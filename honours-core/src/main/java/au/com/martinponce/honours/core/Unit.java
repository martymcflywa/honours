package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class Unit implements IUnit {

  private String id;
  private int mark;

  Unit(String id, int mark) {
    this.id = validate(id);
    this.mark = validate(mark);
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public int mark() {
    return mark;
  }

  private String validate(String id) {
    Validate.notEmpty(id, "Unit id must not be empty");
    return id;
  }

  private int validate(int mark) {
    Validate.inclusiveBetween(
        Rules.MIN_MARK,
        Rules.MAX_MARK,
        mark,
        "Mark must be between {} and {}",
        Rules.MIN_MARK,
        Rules.MAX_MARK);
    return mark;
  }

  @Override
  public int compareTo(IUnit o) {
    if (mark == o.mark()) return 0;
    return mark < o.mark() ? -1 : 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Unit)) return false;
    Unit that = (Unit) o;
    return id.equals(that.id)
        && mark == that.mark();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, mark);
  }
}
