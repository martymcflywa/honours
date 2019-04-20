package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAssess extends Remote {

  /**
   * Assess course unit marks for honours eligibility.
   * @param request The request object containing student id, course id and
   * unit marks.
   * @return Message indicating honours eligibility.
   * @throws RemoteException when an error occurs during assessment.
   */
  String assess(IRequest request) throws RemoteException;

  /**
   * Save course unit marks.
   * @param request The request object containing student id, course id and
   * unit marks.
   * @throws RemoteException when an error occurs during save.
   */
  void save(IRequest request) throws RemoteException;
}