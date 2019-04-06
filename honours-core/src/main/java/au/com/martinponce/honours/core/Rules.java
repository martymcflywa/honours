package au.com.martinponce.honours.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

public class Rules {

  public static final int MIN_MARKS = 12;
  public static final int MAX_MARKS = 30;
  public static final int PASS_MARK = 50;
  public static final double AVG_FOR_QUALIFY = 70d;
  public static final double AVG_FOR_ASSESSMENT = 80d;
  public static final double AVG_FOR_PERMISSION = 70d;
  public static final int MAX_FAILS = 5;
  public static final int TOP_COUNT = 8;

  /**
   * Returns the appropriate assessment result, based on the total average,
   * and top marks average.
   * @param marks The course unit marks
   * @return The result.
   */
  public static Result apply(Collection<Integer> marks) {
    double average = average(marks);
    double topEightAverage = average(top(marks, Rules.TOP_COUNT));

    if (isDenied(marks))
      return Result.DENIED;
    if (isQualified(average))
      return Result.QUALIFIED;
    if (isAssessmentRequired(topEightAverage))
      return Result.NEED_ASSESSMENT;
    if (isPermissionRequired(topEightAverage))
      return Result.NEED_PERMISSION;

    return Result.NOT_QUALIFIED;
  }

  /**
   * Determine if student is denied for honours study.
   * Cannot have more than MAX_FAIL failed/incomplete units.
   * @param marks The course unit marks.
   * @return true if failed/incomplete more than MAX_FAIL units.
   */
  private static boolean isDenied(Collection<Integer> marks) {
    return marks.stream()
        .filter(i -> i < Rules.PASS_MARK)
        .count() > Rules.MAX_FAILS;
  }

  /**
   * Determine if student is qualified for honours study.
   * @param average The overall average.
   * @return true if average is greater than or equal to AVG_FOR_QUALIFY.
   */
  private static boolean isQualified(double average) {
    return average >= Rules.AVG_FOR_QUALIFY;
  }

  /**
   * Determine if student requires further assessment for honours study.
   * @param average The top eight highest scoring units average.
   * @return true if top eight highest scoring units average is higher than
   * AVG_FOR_ASSESSMENT.
   */
  private static boolean isAssessmentRequired(double average) {
    return average >= Rules.AVG_FOR_ASSESSMENT;
  }

  /**
   * Determine if student requires special permission for honours study.
   * @param average The top eight highest scoring units average.
   * @return true if top eight highest scoring units average is higher than
   * AVG_FOR_PERMISSION.
   */
  private static boolean isPermissionRequired(double average) {
    return average >= Rules.AVG_FOR_PERMISSION;
  }

  /**
   * Calculate average of submitted course marks.
   * @param marks The course unit marks.
   * @return average of submitted course marks, at 2 decimal places.
   */
  public static double average(Collection<Integer> marks) {
    return average(marks.stream());
  }

  /**
   * Calculate average of submitted course marks.
   * @param marks The course unit marks.
   * @return average of submitted course marks, at 2 decimal places.
   */
  public static double average(Stream<Integer> marks) {
    return marks.mapToInt(i -> i)
        .average()
        .orElseThrow(() -> new NullPointerException("Mark average is null"));
  }

  /**
   * Get top scoring number of marks.
   * @param marks The course unit marks.
   * @param n The number of marks to return.
   * @return The top scoring number of marks.
   */
  public static Stream<Integer> top(Collection<Integer> marks, int n) {
    return marks.stream()
        .sorted(Comparator.reverseOrder())
        .limit(n);
  }
}
