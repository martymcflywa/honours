package au.com.martinponce.honours.interfaces;

import java.io.Serializable;

public interface IUnit extends Serializable {
  String id();
  boolean equals(Object that);
  int hashCode();
}
