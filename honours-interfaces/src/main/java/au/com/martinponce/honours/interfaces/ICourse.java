package au.com.martinponce.honours.interfaces;

import java.io.Serializable;
import java.util.Collection;

public interface ICourse extends Serializable {
  String id();
  Collection<IUnit> unitMarks();
  IUnit get(String unitId);
  void add(String unitId, int mark);
  Collection<IUnit> top(int n);
  int markTally();
  boolean hasMaxUnits();
  boolean equals(Object that);
  int hashCode();
}
