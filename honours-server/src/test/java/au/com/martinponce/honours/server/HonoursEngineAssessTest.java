package au.com.martinponce.honours.server;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IPersist;
import au.com.martinponce.honours.interfaces.IRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Honours engine assessment test")
class HonoursEngineAssessTest {

  private String studentId = "student123";
  private String courseId = "course123";

  @Test
  @DisplayName("Assess denied")
  void assessDenied() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK - 1));

    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.DENIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess qualified")
  void assessQualified() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), (int) Rules.AVG_FOR_QUALIFY));

    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.QUALIFIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess need assessment")
  void assessNeedAssessment() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), (int) Rules.AVG_FOR_ASSESSMENT));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MAX_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));

    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.NEED_ASSESSMENT_MESSAGE));
  }

  @Test
  @DisplayName("Assess need permission")
  void assessNeedPermission() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), (int) Rules.AVG_FOR_PERMISSION));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));

    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.NEED_PERMISSION_MESSAGE));
  }

  @Test
  @DisplayName("Assess not qualified")
  void assessNotQualified() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(),
            (int) Rules.AVG_FOR_PERMISSION - 1));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));

    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.NOT_QUALIFIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess null studentId")
  void assessNullId() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), i));
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(NullPointerException.class,
        () -> sut.assess(new Request(null, course)));
  }

  @Test
  @DisplayName("Assess empty studentId")
  void assessEmptyId() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), i));
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(IllegalArgumentException.class,
        () -> sut.assess(new Request("", course)));
  }

  @Test
  @DisplayName("Assess null course")
  void assessNullCourse() throws Exception {
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(NullPointerException.class,
        () -> sut.assess(new Request(studentId, null)));
  }

  @Test
  @DisplayName("Assess empty unitMarks")
  void assessEmptyMarks() throws Exception {
    ICourse course = new Course(courseId);
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(IllegalArgumentException.class,
        () -> sut.assess(new Request(studentId, course)));
  }

  @Test
  @DisplayName("Save throws exception test")
  void saveThrowsException() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), i));
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(RemoteException.class,
        () -> sut.save(new Request(studentId, course)));
  }

  @Test
  @DisplayName("Save success test")
  void saveSuccessTest() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), i));
    HonoursEngine sut = new HonoursEngine(new TestPersist());
    assertDoesNotThrow(() -> sut.save(new Request(studentId, course)));
  }

  @Test
  @DisplayName("Load throws exception test")
  void loadThrowsException() throws Exception {
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(RemoteException.class,
        () -> sut.load(studentId, courseId));
  }

  @Test
  @DisplayName("Load success test")
  void loadSuccessTest() throws Exception {
    HonoursEngine sut = new HonoursEngine(new TestPersist());
    assertDoesNotThrow(() -> sut.load(studentId, courseId));
  }

  @Test
  @DisplayName("Delete throws exception test")
  void deleteThrowsException() throws Exception {
    HonoursEngine sut = new HonoursEngine(new PersistNotImplemented());
    assertThrows(RemoteException.class,
        () -> sut.delete(studentId, courseId));
  }

  @Test
  @DisplayName("Delete success test")
  void deleteSuccessTest() throws Exception {
    HonoursEngine sut = new HonoursEngine(new TestPersist());
    assertDoesNotThrow(() -> sut.delete(studentId, courseId));
  }

  private class TestPersist implements IPersist {

    @Override
    public void put(IRequest request) throws RemoteException {
      // success
    }

    @Override
    public IRequest get(String studentId, String courseId) throws RemoteException {
      return null;
    }

    @Override
    public void delete(String studentId, String courseId) throws RemoteException {
      // success
    }
  }

  private class PersistNotImplemented implements IPersist {

    @Override
    public void put(IRequest request) throws RemoteException {
      throw new RemoteException("Put not implemented");
    }

    @Override
    public IRequest get(String studentId, String courseId) throws RemoteException {
      throw new RemoteException("Get not implemented");
    }

    @Override
    public void delete(String studentId, String courseId) throws RemoteException {
      throw new RemoteException("Get not implemented");
    }
  }
}
