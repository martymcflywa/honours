package au.com.martinponce.honours.persist.hibernate;

import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.Objects;

class TestConfig {
  private static final String URL_KEY = "hibernate.connection.url";
  private static final String USER_KEY = "hibernate.connection.username";
  private static final String PASS_KEY = "hibernate.connection.password";
  private static final String AUTO_KEY = "hibernate.hbm2ddl.auto";

  private static final String URL_VAL =
      "jdbc:mysql://remotemysql.com/zSUUyPxsHR";
  private static final String USER_VAL = "zSUUyPxsHR";
  private static final String PASS_VAL = "fsdpTAhxcU";
  private static final String AUTO_VAL = "create-drop";

  static Configuration load() {
    ClassLoader classLoader = Repository.class.getClassLoader();
    File file = new File(
        Objects.requireNonNull(
            classLoader.getResource(Repository.CONFIG_FILENAME)).getFile());
    Configuration config = new Configuration().configure(file);
    config.setProperty(URL_KEY, URL_VAL);
    config.setProperty(USER_KEY, USER_VAL);
    config.setProperty(PASS_KEY, PASS_VAL);
    config.setProperty(AUTO_KEY, AUTO_VAL);
    return config;
  }
}
