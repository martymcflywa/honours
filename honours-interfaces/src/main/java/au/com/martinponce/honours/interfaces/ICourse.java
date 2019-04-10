package au.com.martinponce.honours.interfaces;

import java.io.Serializable;
import java.util.Map;

public interface ICourse extends Serializable {
  String id();
  Map<IUnit, IMark> marks();
  IMark get(IUnit key);
  void put(String unitId, int mark);
  Map<IUnit, IMark> top(int n);
  int markTally();
  boolean hasMaxUnits();
  boolean equals(Object that);
  int hashCode();
}
