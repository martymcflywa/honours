package au.com.martinponce.honours.server;

import au.com.martinponce.honours.interfaces.IAuth;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Auth extends UnicastRemoteObject implements IAuth {

  private static final Logger LOG = LoggerFactory.getLogger(Auth.class);

  Auth() throws RemoteException {
    super();
  }

  /**
   * Super basic authentication implementation. Might improve it if i have time
   * later and remove hardcoded user/pass. Maybe store pass hash in db for
   * stage 2.
   * @param user The username.
   * @param pass The password.
   * @return true if successfully authenticated, else throw.
   * @throws RemoteException on unexpected error.
   */
  @Override
  public boolean login(String user, String pass) throws RemoteException {
    try {
      Validate.notEmpty(user, "User must not be null or empty");
      Validate.notEmpty(pass, "Pass must not be null or empty");
    } catch (NullPointerException | IllegalArgumentException e) {
      LOG.error(e.getMessage());
      throw new RemoteException(e.getMessage());
    }

    if (user.equals("admin") && pass.equals("admin")) {
      LOG.info("Login successful");
      return true;
    }

    String message = "Login failed";
    LOG.error(message);
    throw new RemoteException(message);
  }
}
