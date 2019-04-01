package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IAssess extends Remote {

  /**
   * Assess course marks for honours eligibility.
   * @param id Student id.
   * @param marks A collection of course marks.
   * @return Message indicating honours eligibility.
   * @throws RemoteException
   */
  String assess(String id, List<Integer> marks) throws RemoteException;
}