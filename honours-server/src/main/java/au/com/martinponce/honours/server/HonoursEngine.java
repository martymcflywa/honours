package au.com.martinponce.honours.server;

import au.com.martinponce.honours.interfaces.IAssess;
import org.apache.commons.lang3.Validate;

import java.rmi.RemoteException;
import java.util.List;

public class HonoursEngine implements IAssess {

  @Override
  public String assess(String id, List<Integer> marks) throws RemoteException {

    return null;
  }

  void validateId(String id) {
    Validate.notEmpty(id);
  }

  void validateMarks(List<Integer> marks) {
    int min = 12;
    int max = 30;
    Validate.notEmpty(marks);
    Validate.isTrue(marks.size() >= min, "Minimum %d marks", min);
    Validate.isTrue(marks.size() < max, "Maximum %d marks", max);
  }
}
