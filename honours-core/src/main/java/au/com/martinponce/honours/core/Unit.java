package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

public class Unit implements IUnit {

  private String id;

  public Unit(String id) {
    this.id = validate(id);
  }

  @Override
  public String id() {
    return id;
  }

  private String validate(String id) {
    Validate.notEmpty(id, "Unit id must not be empty");
    return id;
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof IUnit
        && id.equals(((IUnit) that).id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
