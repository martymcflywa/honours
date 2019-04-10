package au.com.martinponce.honours.server;

import au.com.martinponce.honours.interfaces.IAuth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Auth test")
class AuthTest {

  @Test
  @DisplayName("User and pass correct test")
  void userPassCorrect() throws Exception {
    String user = "admin";
    String pass = "admin";
    IAuth sut = new Auth();
    assertDoesNotThrow(() -> sut.login(user, pass));
  }

  @Test
  @DisplayName("User correct pass incorrect test")
  void userCorrectPassIncorrect() throws Exception {
    String user = "admin";
    String pass = "bar";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(user, pass));
  }

  @Test
  @DisplayName("User incorrect pass correct test")
  void userIncorrectPassCorrect() throws Exception {
    String user = "foo";
    String pass = "admin";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(user, pass));
  }

  @Test
  @DisplayName("User and pass incorrect test")
  void userPassIncorrect() throws Exception {
    String user = "foo";
    String pass = "bar";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(user, pass));
  }

  @Test
  @DisplayName("User null test")
  void userNull() throws Exception {
    String pass = "bar";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(null, pass));
  }

  @Test
  @DisplayName("User empty test")
  void userEmpty() throws Exception {
    String user = "";
    String pass = "bar";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(user, pass));
  }

  @Test
  @DisplayName("Pass null test")
  void passNull() throws Exception {
    String user = "foo";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(user, null));
  }

  @Test
  @DisplayName("Pass empty test")
  void passEmpty() throws Exception {
    String user = "foo";
    String pass = "";
    IAuth sut = new Auth();
    assertThrows(RemoteException.class, () -> sut.login(user, pass));
  }
}
