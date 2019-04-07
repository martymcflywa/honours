package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.IMark;
import org.apache.commons.lang3.Validate;

public class Mark implements IMark {

  private int attempts;
  private int mark;

  Mark(int mark) {
    attempts = 1;
    this.mark = validate(mark);
  }

  @Override
  public int attempts() {
    return attempts;
  }

  @Override
  public int mark() {
    return mark;
  }

  @Override
  public void addAttempt() {
    attempts++;
  }

  @Override
  public void set(int mark) {
    this.mark = validate(mark);
  }

  private int validate(int mark) {
    Validate.inclusiveBetween(
        0, 100, mark, "Mark must be between 0 and 100");
    return mark;
  }

  @Override
  public int compareTo(IMark o) {
    if (mark == o.mark()) return 0;
    return mark < o.mark() ? -1 : 1;
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof Mark
        && mark == ((Mark) that).mark;
  }

  @Override
  public int hashCode() {
    return mark;
  }
}
