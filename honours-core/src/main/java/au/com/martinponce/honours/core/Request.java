package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IRequest;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class Request implements IRequest {

  private final String STUDENT_ID;
  private final ICourse COURSE;

  public Request(String studentId, ICourse course) {
    STUDENT_ID = validate(studentId);
    COURSE = validate(course);
  }

  @Override
  public String studentId() {
    return STUDENT_ID;
  }

  @Override
  public ICourse course() {
    return COURSE;
  }

  /**
   * Validate a string.
   * Must not be null or empty string.
   * @param string The string to validate.
   * @return The string, if valid, else throws.
   */
  private String validate(String string) {
    Validate.notEmpty(string, "Student id must not be empty or null");
    return string;
  }

  /**
   * Validate a course.
   * Marks must not be empty.
   * Marks tally must be between min and max inclusive.
   * @param course The course to validate.
   * @return The collection of unitMarks, if valid, else throws.
   */
  private ICourse validate(ICourse course) {
    Validate.notEmpty(course.unitMarks(), "Marks collection must not be empty");
    Validate.inclusiveBetween(
        Rules.MIN_MARK_COUNT,
        Rules.MAX_MARK_COUNT,
        course.markTally(),
        String.format(
            "Number of unitMarks must be between %d and %d inclusive",
            Rules.MIN_MARK_COUNT, Rules.MAX_MARK_COUNT));
    return course;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Request)) return false;
    Request that = (Request) o;
    return Objects.equals(studentId(), that.studentId())
        && Objects.equals(course(), that.course());
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId(), course());
  }
}
