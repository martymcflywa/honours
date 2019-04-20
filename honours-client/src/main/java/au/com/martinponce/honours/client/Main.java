package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Names;
import au.com.martinponce.honours.interfaces.IAssess;
import au.com.martinponce.honours.interfaces.IAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static java.lang.System.exit;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    init();
  }

  private static void init() {
    try {
      IAuth auth = (IAuth) Naming.lookup(Names.AUTH_NAME);
      IAssess assessEngine = (IAssess) Naming.lookup(Names.HONOURS_NAME);
      CommandlineInterface cli = new CommandlineInterface(auth, assessEngine);
      cli.run();
    } catch (MalformedURLException e) {
      LOG.error("Malformed url", e);
      exit(1);
    } catch (NotBoundException e) {
      LOG.error("Interface not bound", e);
      exit(1);
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      exit(1);
    }
  }
}
