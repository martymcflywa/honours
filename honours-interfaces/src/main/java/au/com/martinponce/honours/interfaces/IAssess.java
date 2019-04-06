package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAssess extends Remote {

  /**
   * Assess course marks for honours eligibility.
   * @param request The request object containing student id and marks.
   * @return Message indicating honours eligibility.
   * @throws RemoteException
   */
  String assess(IRequest request) throws RemoteException;
}