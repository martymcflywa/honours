package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Rules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

import static java.lang.System.console;

class CommandlineInterface {

  private String id;
  private Collection<Integer> marks;

  private static final Logger LOG =
      LoggerFactory.getLogger(CommandlineInterface.class);

  void start() {
    LOG.info("Welcome to the honours assessment, follow the instructions to " +
        "determine your eligibility for honours study");
    id = setId();
    marks = setMarks();
  }

  private String setId() {
    return console().readLine("Enter your id: ");
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
        input = console().readLine(String.format("Mark %d: ", count));
        int mark = Integer.parseInt(input);
        if (!valid(mark)) {
          LOG.error("Mark must be between 0 and 100, try again");
          continue;
        }
        marks.add(Integer.parseInt(input));
        count++;
      } catch (NumberFormatException | NullPointerException e) {
        if (input != null && input.equals("q")) {
          if (marks.size() < Rules.MIN_MARKS) {
            LOG.warn("Need a minimum {} marks, enter another {} marks",
                Rules.MIN_MARKS, Rules.MIN_MARKS - marks.size());
            continue;
          }
          break;
        }
        LOG.error("Input an integer, or q to quit");
      }
    } while (canAddMore(marks));

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

  String getId() {
    return id;
  }

  Collection<Integer> getMarks() {
    return marks;
  }
}
