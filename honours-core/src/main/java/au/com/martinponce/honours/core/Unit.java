package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class Unit implements IUnit {

  private final String ID;
  private final int MARK;

  Unit(String id, int mark) {
    ID = validate(id);
    MARK = validate(mark);
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public int mark() {
    return MARK;
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
    if (MARK == o.mark()) return 0;
    return MARK < o.mark() ? -1 : 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Unit)) return false;
    Unit that = (Unit) o;
    return ID.equals(that.ID)
        && MARK == that.mark();
  }

  @Override
  public int hashCode() {
    return Objects.hash(ID, MARK);
  }
}
