package au.com.martinponce.honours.client;

import static java.lang.System.console;

public class ConsoleInput implements IUserInput {

  @Override
  public String read() {
    String CURSOR = "> ";
    return console().readLine(CURSOR);
  }
}
