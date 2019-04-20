package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface IPersist extends Remote {
  void put(IRequest request) throws RemoteException;
  Collection<IRequest> get(IRequest request) throws RemoteException;
  void delete(IRequest request) throws RemoteException;
}
