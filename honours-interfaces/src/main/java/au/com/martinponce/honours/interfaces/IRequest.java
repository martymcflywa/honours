package au.com.martinponce.honours.interfaces;

import java.io.Serializable;
import java.util.Collection;

public interface IRequest extends Serializable {
  String id();
  Collection<Integer> marks();
}
