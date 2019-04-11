package au.com.martinponce.honours.server;

import au.com.martinponce.honours.core.Result;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.IAssess;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HonoursEngine extends UnicastRemoteObject implements IAssess {

  private static final long serialVersionUID = 1L;
  static final String QUALIFIED_MESSAGE = "QUALIFIES FOR HONOURS STUDY!";
  static final String NEED_ASSESSMENT_MESSAGE = "MAY HAVE A GOOD CHANCE! Need" +
      " further assessment!";
  static final String NEED_PERMISSION_MESSAGE = "MAY HAVE A CHANCE! Must be " +
      "carefully reassessed and get the coordinator's special permission!";
  static final String NOT_QUALIFIED_MESSAGE = "DOES NOT QUALIFY FOR HONOURS " +
      "STUDY! Try Masters By Coursework.";
  static final String DENIED_MESSAGE = "TOO MANY FAILURES!";

  private static final Logger LOG =
      LoggerFactory.getLogger(HonoursEngine.class);

  HonoursEngine() throws RemoteException {
    super(0);
  }

  /**
   * @param request The request object containing student id and marks.
   * @return The assessment result.
   * @throws RemoteException When errors occur.
   */
  @Override
  public String assess(IRequest request) throws RemoteException {
    try {
      String response = response(request.studentId(), request.course(),
          Rules.apply(request.course()));
      LOG.info(response);
      return response;
    } catch (Exception e) {
      LOG.error("An unexpected error occurred", e);
      throw new RemoteException(e.getMessage());
    }
  }

  private String response(String id, ICourse course, Result result) {
    double average = Rules.average(course.marks());
    double topEightAverage = Rules.average(course.top(Rules.TOP_COUNT));
    switch (result) {
      case QUALIFIED:
        return String.format("%s, %.2f, %s", id, average, QUALIFIED_MESSAGE);
      case NEED_ASSESSMENT:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, NEED_ASSESSMENT_MESSAGE);
      case NEED_PERMISSION:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, NEED_PERMISSION_MESSAGE);
      case NOT_QUALIFIED:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, NOT_QUALIFIED_MESSAGE);
      case DENIED:
        return String.format("%s, %.2f, %.2f, %s", id, average,
            topEightAverage, DENIED_MESSAGE);
      default:
        throw new IllegalArgumentException("Unsupported result: " + result.name());
    }
  }
}
