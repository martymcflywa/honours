package au.com.martinponce.honours.persist;

import au.com.martinponce.honours.core.Names;
import au.com.martinponce.honours.interfaces.IPersist;
import au.com.martinponce.honours.persist.hibernate.IRepository;
import au.com.martinponce.honours.persist.hibernate.Persist;
import au.com.martinponce.honours.persist.hibernate.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

import static java.lang.System.exit;

class Main {

  private static final String HIBERNATE_CONFIG = "hibernate.cfg.xml";
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    initSecurity();
    initServer();
  }

  private static void initServer() {
    try {
      LocateRegistry.getRegistry(Names.RMI_PORT);
      InputStream config = getConfig();
      IRepository repository = new Repository(config);
      IPersist persist = new Persist(repository);
      Naming.rebind(Names.PERSIST_NAME, persist);
      Arrays.stream(Naming.list(Names.BASE_NAME))
          .forEach(i -> LOG.info("Persist bound {}", i));
      LOG.info("Persist waiting");
    } catch (RemoteException e) {
      LOG.error("RemoteException", e);
      exit(1);
    } catch (MalformedURLException e) {
      LOG.error("MalformedURLException", e);
      exit(1);
    }
  }

  private static void initSecurity() {
    if (System.getSecurityManager() ==  null)
      System.setSecurityManager(new SecurityManager());
  }

  private static InputStream getConfig() {
    return Main.class.getClassLoader().getResourceAsStream(HIBERNATE_CONFIG);
  }
}
