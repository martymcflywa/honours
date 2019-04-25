package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.IAssess;
import au.com.martinponce.honours.interfaces.IAuth;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

import static java.lang.System.exit;

class CommandlineInterface {

  private final IUserInput INPUT;
  private final IAuth AUTH;
  private final IAssess ASSESS_ENGINE;

  private boolean isAuthenticated;

  private static final Logger LOG =
      LoggerFactory.getLogger(CommandlineInterface.class);

  CommandlineInterface(IUserInput input, IAuth auth, IAssess assessEngine) {
    INPUT = input;
    AUTH = auth;
    ASSESS_ENGINE = assessEngine;
  }

  void run() {
    try {
      LOG.info("Welcome to the honours assessment");
      LOG.info("Follow the instructions to determine honours eligibility");
      String input;
      do {
        if (!isAuthenticated)
          authUser();

        String studentId = setProperty("studentId", false);
        String courseId = setProperty("courseId", false);

        ICourse course = loadCourse(studentId, courseId);
        if (course == null)
          course = setCourse(studentId, courseId);

        String response = sendRequest(studentId, course);
        LOG.info("[OUTPUT] {}", response);
        LOG.info("[INPUT ] Press enter to try again, or q to quit");
        input = INPUT.read();
        onQuitControl(input);
      } while (input.isEmpty());
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      LOG.info("Try again");
      run();
    } catch (QuitInterrupt e) {
      shutdown();
    }
  }

  void authUser() throws RemoteException, QuitInterrupt {
    String user = setProperty("username", false);
    String pass = setProperty("password", false);
    isAuthenticated = AUTH.login(user, pass);
  }

  ICourse loadCourse(String studentId, String courseId)
      throws RemoteException, QuitInterrupt {
    try {
      IRequest request = ASSESS_ENGINE.load(studentId, courseId);
      if (request == null) {
        LOG.info(
            "[OUTPUT] Existing course not found for {} {}",
            studentId,
            courseId);
        return null;
      }
      LOG.info("[OUTPUT] Existing course found {} {}", studentId, courseId);
      ICourse course = new Course(courseId);
      request.course().unitMarks().values()
          .forEach(i -> {
            LOG.info("[OUTPUT] {} {}", i.id(), i.mark());
            course.add(i.id(), i.mark());
          });
      return deleteCourse(studentId, courseId) ? null : course;
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  ICourse setCourse(String studentId, String courseId) throws QuitInterrupt {
    LOG.info("Enter unitId and mark, for a minimum of {} units, " +
        "and a maximum of {} units, or e to end input, q to quit",
        Rules.MIN_MARK_COUNT,
        Rules.MAX_MARK_COUNT);
    ICourse course = new Course(courseId);
    do {
      try {
        int tally = course.markTally();
        String unitId = setProperty("unitId " + (tally + 1), true);
        onEndControl(unitId, tally);
        String mark = setProperty("mark " + (tally + 1), true);
        onEndControl(mark, tally);
        course.add(unitId, Integer.parseInt(mark));
      } catch (EndInputInterrupt e) {
        break;
      } catch (NumberFormatException e) {
        LOG.error("Mark must be an integer, try again");
      } catch (IllegalArgumentException e) {
        LOG.error(e.getMessage());
      }
    } while (!course.hasMaxUnits());
    LOG.info("User input complete");
    saveCourse(studentId, course);
    return course;
  }

  void saveCourse(String studentId, ICourse course) throws QuitInterrupt {
    try {
      String input;
      do {
        LOG.info(
            "[INPUT ] s to save course details, or c to continue, or q to quit");
        input = INPUT.read();
        onCancelControl(input, "c");
        onQuitControl(input);
        onSaveControl(input, studentId, course);
      } while (input.isEmpty());
    } catch (CancelInputInterrupt e) {
      LOG.info("Course not saved");
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      LOG.info("Try again");
      saveCourse(studentId, course);
    }
  }

  boolean deleteCourse(String studentId, String courseId) throws QuitInterrupt {
    try {
      String input;
      boolean output;
      do {
        LOG.info(
            "[INPUT ] k to keep, or d to delete, or q to quit");
        input = INPUT.read();
        onCancelControl(input, "k");
        onQuitControl(input);
        output = onDeleteControl(input, studentId, courseId);
      } while (input.isEmpty());
      return output;
    } catch (CancelInputInterrupt e) {
      LOG.info("Course not deleted");
      return false;
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      LOG.info("Course not deleted");
      return false;
    }
  }

  String sendRequest(String studentId, ICourse course)
      throws RemoteException {
    LOG.info("Sending request to server for assessment");
    try {
      return ASSESS_ENGINE.assess(new Request(studentId, course));
    } catch (NullPointerException | IllegalArgumentException e) {
      throw new RemoteException(e.getMessage());
    }
  }

  private String setProperty(String property, boolean canEndInput) throws QuitInterrupt {
    String input;
    String endMessage = canEndInput ? ", or e to end input" : "";
    do {
      LOG.info("[INPUT ] Enter {}{}, or q to quit",
          property,
          endMessage);
      input = INPUT.read();
      onQuitControl(input);
    } while (input.isEmpty());
    return input;
  }

  private void onSaveControl(String input, String studentId, ICourse course)
      throws RemoteException {
    if (input == null || !input.equals("s"))
      return;

    LOG.info("Saving marks for {} {}", studentId, course.id());
    ASSESS_ENGINE.save(new Request(studentId, course));
  }

  private boolean onDeleteControl(String input, String studentId, String courseId)
      throws RemoteException {
    if (input == null || !input.equals("d"))
      return false;

    LOG.info("Deleting marks for {} {}", studentId, courseId);
    ASSESS_ENGINE.delete(studentId, courseId);
    return true;
  }

  private void onCancelControl(String input, String control) throws CancelInputInterrupt {
    if (input == null || !input.equals(control))
      return;
    throw new CancelInputInterrupt();
  }

  private void onEndControl(String input, int tally) throws EndInputInterrupt {
    if (input == null || !input.equals("e"))
      return;

    if (tally < Rules.MIN_MARK_COUNT) {
      LOG.error("Still need to input {} more unit marks",
          Rules.MIN_MARK_COUNT - tally);
      return;
    }
    throw new EndInputInterrupt();
  }

  private void onQuitControl(String input) throws QuitInterrupt {
    if (input != null && input.equals("q"))
      throw new QuitInterrupt();
  }

  private void shutdown() {
    LOG.info("Shutting down");
    exit(0);
  }

  class CancelInputInterrupt extends Exception {
    CancelInputInterrupt() {
      super();
    }
  }

  class EndInputInterrupt extends Exception {
    EndInputInterrupt() {
      super();
    }
  }

  class QuitInterrupt extends Exception {
    QuitInterrupt() {
      super();
    }
  }
}
