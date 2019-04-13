package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPersist extends Remote {
  void put(IRequest request) throws RemoteException;
  IRequest get(IRequest request) throws RemoteException;
  void delete(IRequest request) throws RemoteException;
}
