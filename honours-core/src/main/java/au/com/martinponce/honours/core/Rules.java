package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;

import java.util.Map;
import java.util.stream.Stream;

public class Rules {

  static final int MIN_MARK = 0;
  static final int MAX_MARK = 100;
  public static final int MIN_MARK_COUNT = 12;
  public static final int MAX_MARK_COUNT = 30;
  public static final int PASS_MARK = 50;
  public static final double AVG_FOR_QUALIFY = 70d;
  public static final double AVG_FOR_ASSESSMENT = 80d;
  public static final double AVG_FOR_PERMISSION = 70d;
  public static final int TOP_COUNT = 8;
  private static final int MAX_FAILS = 5;

  /**
   * Returns the appropriate assessment result, based on the total average,
   * and top unitMarks average.
   * @param course The course.
   * @return The result.
   */
  public static Result apply(ICourse course) {
    double average = average(course.unitMarks());
    double topEightAverage = average(course.top(Rules.TOP_COUNT));

    if (isDenied(course.unitMarks()))
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
   * @param unitMarks The course unit marks.
   * @return true if failed/incomplete more than MAX_FAIL units.
   */
  private static boolean isDenied(Map<String, IUnit> unitMarks) {
    return unitMarks.values().stream()
        .filter(i -> i.mark() < Rules.PASS_MARK)
        .count() > Rules.MAX_FAILS;
  }

  /**
   * Determine if student is qualified for honours study.
   * @param average The average unitMarks.
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
   * Calculate average of submitted course unitMarks.
   * @param unitMarks The course unit marks.
   * @return average of unit marks.
   */
  public static double average(Map<String, IUnit> unitMarks) {
    return average(unitMarks.values().stream());
  }

  /**
   * Calculate average of submitted course unitMarks.
   * @param unitMarks The course unit marks stream.
   * @return average of unit marks.
   */
  private static double average(Stream<IUnit> unitMarks) {
    return unitMarks.mapToInt(IUnit::mark)
        .average()
        .orElseThrow(() -> new NullPointerException("Mark average is null"));
  }
}
