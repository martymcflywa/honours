package au.com.martinponce.honours.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuth extends Remote {
  boolean login(String user, String pass) throws RemoteException;
}
