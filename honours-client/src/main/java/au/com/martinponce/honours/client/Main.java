package au.com.martinponce.honours.client;

import au.com.martinponce.honours.interfaces.IAssess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

  private static final String NAME = "honours";
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    init();
  }

  private static void init() {
    try {
      IAssess assessEngine = (IAssess) Naming.lookup(NAME);
      CommandlineInterface cli = new CommandlineInterface(assessEngine);
      cli.run();
    } catch (MalformedURLException e) {
      LOG.error("Malformed url", e);
    } catch (NotBoundException e) {
      LOG.error("Interface not bound", e);
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
    }
  }
}
