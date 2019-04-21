package au.com.martinponce.honours.interfaces;

import java.io.Serializable;
import java.util.Map;

public interface ICourse extends Serializable {
  String id();
  Map<String, IUnit> unitMarks();
  IUnit get(String unitId);
  void add(String unitId, int mark);
  Map<String, IUnit> top(int n);
  int markTally();
  boolean hasMaxUnits();
  boolean equals(Object that);
  int hashCode();
}
