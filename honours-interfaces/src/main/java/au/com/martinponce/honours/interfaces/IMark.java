package au.com.martinponce.honours.interfaces;

import java.io.Serializable;

public interface IMark extends Comparable<IMark>, Serializable {
  int attempts();
  int mark();
  void addAttempt();
  void set(int score);
  boolean equals(Object that);
  int hashCode();
}
