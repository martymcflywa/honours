package au.com.martinponce.honours.server;

import au.com.martinponce.honours.interfaces.IAssess;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Stream;

public class HonoursEngine implements IAssess {

  static final int MIN_MARKS = 12;
  static final int MAX_MARKS = 30;
  static final int PASS_MARK = 50;
  static final double AVG_FOR_QUALIFY = 70d;
  static final double AVG_FOR_ASSESSMENT = 80d;
  static final double AVG_FOR_PERMISSION = 70d;
  private static final int MAX_FAILS = 5;
  private static final int TOP_COUNT = 8;

  static final String QUALIFIED_MESSAGE = "QUALIFIES FOR HONOURS STUDY!";
  static final String NEED_ASSESSMENT_MESSAGE = "MAY HAVE A GOOD CHANCE! Need" +
      " further assessment!";
  static final String NEED_PERMISSION_MESSAGE = "MAY HAVE A CHANCE! Must be " +
      "carefully reassessed and get the coordinator's special permission";
  static final String NOT_QUALIFIED_MESSAGE = "DOES NOT QUALIFY FOR HONOURS " +
      "STUDY! Try Masters By Coursework.";
  static final String DENIED_MESSAGE = "TOO MANY FAILURES!";

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * @param id The student's id.
   * @param marks The course unit marks.
   * @return The assessment result.
   * @throws RemoteException When errors occur.
   */
  @Override
  public String assess(String id, Collection<Integer> marks) throws RemoteException {
    try {
      validateId(id);
      validateMarks(marks);
      return response(id, marks, assess(marks));
    } catch (Exception e) {
      logger.error("An unexpected error occurred", e);
      throw new RemoteException(e.getMessage());
    }
  }

  private String response(String id, Collection<Integer> marks, Result result) {
    double average = average(marks);
    double topEightAverage = average(top(marks, TOP_COUNT));
    switch (result) {
      case QUALIFIED:
        return String.format("%s, %.2f, %s", id, average, QUALIFIED_MESSAGE);
      case NEED_ASSESSMENT:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, NEED_ASSESSMENT_MESSAGE);
      case NEED_PERMISSION:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, NEED_PERMISSION_MESSAGE);
      case NOT_QUALIFIED:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, NOT_QUALIFIED_MESSAGE);
      case DENIED:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, DENIED_MESSAGE);
      default:
        throw new IllegalArgumentException("Unsupported result: " + result.name());
    }
  }

  /**
   * Returns the appropriate assessment result, based on the total average,
   * and top marks average.
   * @param marks The course unit marks
   * @return The result.
   */
  private Result assess(Collection<Integer> marks) {
    double average = average(marks);
    double topEightAverage = average(top(marks, TOP_COUNT));

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
  private boolean isDenied(Collection<Integer> marks) {
    return marks.stream()
        .filter(i -> i < PASS_MARK)
        .count() > MAX_FAILS;
  }

  /**
   * Determine if student is qualified for honours study.
   * @param average The overall average.
   * @return true if average is greater than or equal to AVG_FOR_QUALIFY.
   */
  private boolean isQualified(double average) {
    return average >= AVG_FOR_QUALIFY;
  }

  /**
   * Determine if student requires further assessment for honours study.
   * @param average The top eight highest scoring units average.
   * @return true if top eight highest scoring units average is higher than
   * AVG_FOR_ASSESSMENT.
   */
  private boolean isAssessmentRequired(double average) {
    return average >= AVG_FOR_ASSESSMENT;
  }

  /**
   * Determine if student requires special permission for honours study.
   * @param average The top eight highest scoring units average.
   * @return true if top eight highest scoring units average is higher than
   * AVG_FOR_PERMISSION.
   */
  private boolean isPermissionRequired(double average) {
    return average >= AVG_FOR_PERMISSION;
  }

  /**
   * Calculate average of submitted course marks.
   * @param marks The course unit marks.
   * @return average of submitted course marks, at 2 decimal places.
   */
  double average(Collection<Integer> marks) {
    return average(marks.stream());
  }

  private double average(Stream<Integer> marks) {
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
  Stream<Integer> top(Collection<Integer> marks, int n) {
    return marks.stream()
        .sorted(Comparator.reverseOrder())
        .limit(n);
  }

  /**
   * Validate student id.
   * Must not be null or empty string.
   * @param id The student's id.
   */
  void validateId(String id) {
    Validate.notEmpty(id);
  }

  /**
   * Validate marks.
   * Must not be empty.
   * Must have at least MIN_MARKS elements.
   * Must not have elements greater than MAX_MARKS.
   * @param marks The course unit marks.
   */
  void validateMarks(Collection<Integer> marks) {
    Validate.notEmpty(marks);
    Validate.isTrue(marks.size() >= MIN_MARKS, "Minimum %d marks", MIN_MARKS);
    Validate.isTrue(marks.size() <= MAX_MARKS, "Maximum %d marks", MAX_MARKS);
  }
}
