package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPersist extends Remote {
  void put(IRequest request) throws RemoteException;
  IRequest get(String studentId, String courseId) throws RemoteException;
  void delete(String studentId, String courseId) throws RemoteException;
}
