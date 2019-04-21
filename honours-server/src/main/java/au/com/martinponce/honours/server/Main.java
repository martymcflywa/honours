package au.com.martinponce.honours.server;

import au.com.martinponce.honours.core.Names;
import au.com.martinponce.honours.interfaces.IPersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

import static java.lang.System.exit;

class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    initSecurity();
    initServer();
  }

  private static void initServer() {
    try {
      LocateRegistry.getRegistry(Names.RMI_PORT);
      IPersist persist = (IPersist) Naming.lookup(Names.PERSIST_NAME);
      Naming.rebind(Names.AUTH_NAME, new Auth());
      Naming.rebind(Names.HONOURS_NAME, new HonoursEngine(persist));
      Arrays.stream(Naming.list(Names.BASE_NAME))
          .forEach(i -> LOG.info("Server bound {}", i));
      LOG.info("Server waiting");
    } catch (NotBoundException e) {
      LOG.error("Interface not bound", e);
      exit(1);
    } catch (MalformedURLException e) {
      LOG.error("Malformed url", e);
      exit(1);
    } catch (RemoteException e) {
      LOG.error("Remote exception", e);
      exit(1);
    }
  }

  private static void initSecurity() {
    if (System.getSecurityManager() ==  null)
      System.setSecurityManager(new SecurityManager());
  }
}
