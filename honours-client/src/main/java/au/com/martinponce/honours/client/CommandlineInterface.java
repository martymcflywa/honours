package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.IAssess;
import au.com.martinponce.honours.interfaces.IAuth;
import au.com.martinponce.honours.interfaces.ICourse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

import static java.lang.System.console;
import static java.lang.System.exit;

class CommandlineInterface {

  private final IAuth AUTH;
  private final IAssess ASSESS_ENGINE;

  private static final String cursor = "> ";
  private static final Logger LOG =
      LoggerFactory.getLogger(CommandlineInterface.class);

  CommandlineInterface(IAuth auth, IAssess assessEngine) {
    AUTH = auth;
    ASSESS_ENGINE = assessEngine;
  }

  void run() {
    try {
      LOG.info("Welcome to the honours assessment, follow the instructions to " +
          "determine your eligibility for honours study");
      String input;
      do {
        authUser();
        String studentId = setProperty("studentId", false);
        String courseId = setProperty("courseId", false);
        ICourse course = setCourse(courseId);

        String response = sendRequest(studentId, course);
        LOG.info("[OUTPUT] {}", response);
        LOG.info("[INPUT] Press enter to try again, or q to quit");
        input = console().readLine(cursor);
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

  private void authUser() throws RemoteException, QuitInterrupt {
    String user = setProperty("username", false);
    String pass = setProperty("password", false);
    AUTH.login(user, pass);
  }

  private ICourse setCourse(String courseId) throws QuitInterrupt {
    LOG.info("Enter your unit id and its mark, for a minimum of {} units, " +
        "and a maximum of {} units, or e to end input, q to quit",
        Rules.MIN_MARK_COUNT,
        Rules.MAX_MARK_COUNT);
    ICourse course = new Course(courseId);
    int tally = 1;
    do {
      try {
        String unitId = setProperty("unit id " + tally, true);
        onEndControl(unitId, tally);
        String mark = setProperty("mark " + tally, true);
        onEndControl(mark, tally);
        course.add(unitId, Integer.parseInt(mark));
        tally++;
      } catch (EndInputInterrupt e) {
        break;
      } catch (NumberFormatException e) {
        LOG.error("Mark must be an integer, try again");
      } catch (IllegalArgumentException e) {
        LOG.error(e.getMessage());
      }
    } while (!course.hasMaxUnits());
    LOG.info("User input complete");
    return course;
  }

  private String sendRequest(String studentId, ICourse course)
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
      LOG.info("[INPUT] Enter your {}{}, or q to quit",
          property,
          endMessage);
      input = console().readLine(cursor);
      onQuitControl(input);
    } while (input.isEmpty());
    return input;
  }

  private void onEndControl(String input, int tally) throws EndInputInterrupt {
    if (input == null || !input.equals("e"))
      return;

    if (tally < Rules.MIN_MARK_COUNT) {
      LOG.error("Still need to enter another {} unitMarks",
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

  private class EndInputInterrupt extends Exception {
    EndInputInterrupt() {
      super();
    }
  }

  private class QuitInterrupt extends Exception {
    QuitInterrupt() {
      super();
    }
  }
}
