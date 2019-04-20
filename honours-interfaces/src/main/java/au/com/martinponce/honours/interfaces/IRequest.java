package au.com.martinponce.honours.interfaces;

import java.io.Serializable;

public interface IRequest extends Serializable {
  String studentId();
  ICourse course();
  boolean equals(Object o);
  int hashCode();
}
