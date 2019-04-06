package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.interfaces.IAssess;

import java.rmi.RemoteException;
import java.util.Collection;

class Client {

  private IAssess assessEngine;

  Client(IAssess assessEngine) {
    this.assessEngine = assessEngine;
  }

  String assess(String id, Collection<Integer> marks)
      throws RemoteException {
    return assessEngine.assess(new Request(id, marks));
  }
}
