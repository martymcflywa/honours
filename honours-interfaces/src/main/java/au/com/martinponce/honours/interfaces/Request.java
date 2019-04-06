package au.com.martinponce.honours.interfaces;

import org.apache.commons.lang3.Validate;

import java.util.Collection;

public class Request implements IRequest {

  private String id;
  private Collection<Integer> marks;

  public Request(String id, Collection<Integer> marks) {
    this.id = validate(id);
    this.marks = validate(marks);
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public Collection<Integer> marks() {
    return marks;
  }

  /**
   * Validate a string.
   * Must not be null or empty string.
   * @param string The string to validate.
   * @return The string, if valid, else throws.
   */
  private String validate(String string) {
    Validate.notEmpty(string);
    return string;
  }

  /**
   * Validate a collection of marks.
   * Must not be empty.
   * Size must be between min and max inclusive.
   * @param marks The marks to validate.
   * @return The collection of marks, if valid, else throws.
   */
  private Collection<Integer> validate(Collection<Integer> marks) {
    Validate.notEmpty(marks);
    Validate.inclusiveBetween(
        Rules.MIN_MARKS,
        Rules.MAX_MARKS,
        marks.size(),
        String.format(
            "Number of marks must be between %d and %d inclusive",
            Rules.MIN_MARKS, Rules.MAX_MARKS));
    return marks;
  }
}
