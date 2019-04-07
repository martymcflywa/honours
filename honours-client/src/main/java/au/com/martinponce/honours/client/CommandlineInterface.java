package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.IAssess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.System.*;

class CommandlineInterface {

  private IAssess assessEngine;

  private static final String cursor = "> ";
  private static final Logger LOG =
      LoggerFactory.getLogger(CommandlineInterface.class);

  CommandlineInterface(IAssess assessEngine) {
    this.assessEngine = assessEngine;
  }

  void run() {
    try {
      LOG.info("Welcome to the honours assessment, follow the instructions to " +
          "determine your eligibility for honours study");
      String input;
      do {
        String id = setId();
        Collection<Integer> marks = setMarks();
        String response = sendRequest(id, marks);
        LOG.info("[OUTPUT] {}", response);
        LOG.info("[INPUT] Enter q to quit, or press enter to try again:");
        input = console().readLine(cursor);
      } while (!isQuit(input));
      shutdown();
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      LOG.info("Try again");
      run();
    }
  }

  private String setId() {
    String input;
    do {
      LOG.info("[INPUT] Enter your id:");
      input = console().readLine(cursor);
    } while (input.isEmpty());
    return input;
  }

  private Collection<Integer> setMarks() {
    LOG.info("Enter your unit marks, minimum of {}, and maximum of {} marks, " +
            "q to quit",
        Rules.MIN_MARKS, Rules.MAX_MARKS);

    String input = null;
    Collection<Integer> marks = new ArrayList<>();
    int count = 1;
    do {
      try {
        LOG.info("[INPUT] Mark {}:", count);
        input = console().readLine(cursor);
        int mark = Integer.parseInt(input);
        if (!valid(mark)) {
          LOG.error("Mark must be between 0 and 100, try again");
          continue;
        }
        marks.add(Integer.parseInt(input));
        count++;
      } catch (NumberFormatException | NullPointerException e) {
        if (isQuit(input)) {
          if (marks.size() < Rules.MIN_MARKS) {
            LOG.error("Need a minimum {} marks, enter another {} marks",
                Rules.MIN_MARKS, Rules.MIN_MARKS - marks.size());
            continue;
          }
          break;
        }
        LOG.error("Input an integer, or q to quit");
      }
    } while (canAddMore(marks));
    LOG.info("User input complete");
    return marks;
  }

  private boolean canAddMore(Collection<Integer> marks) {
    boolean out = marks.size() < Rules.MAX_MARKS;
    if (!out)
      LOG.info("Maximum number of marks reached");
    return out;
  }

  private boolean valid(int mark) {
    return mark >= 0 && mark <= 100;
  }

  private String sendRequest(String id, Collection<Integer> marks)
      throws RemoteException {
    LOG.info("Sending request to server for assessment");
    return assessEngine.assess(new Request(id, marks));
  }

  private boolean isQuit(String input) {
    return input != null && input.equals("q");
  }

  private void shutdown() {
    LOG.info("Shutting down");
    exit(0);
  }
}
