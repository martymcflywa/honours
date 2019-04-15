package au.com.martinponce.honours.interfaces;

import java.io.Serializable;

public interface IUnit extends Serializable {
  String id();
  int mark();
  int compareTo(IUnit o);
  boolean equals(Object that);
  int hashCode();
}
