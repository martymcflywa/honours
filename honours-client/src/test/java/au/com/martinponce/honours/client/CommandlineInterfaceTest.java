package au.com.martinponce.honours.client;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.IAssess;
import au.com.martinponce.honours.interfaces.IAuth;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.rmi.RemoteException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Commandline interface test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandlineInterfaceTest {

  private IUserInput input;
  private IAuth auth;
  private IAssess assess;
  private CommandlineInterface sut;

  @BeforeAll
  void beforeAll() {
    input = new TestInput();
    auth = new TestAuth();
    assess = new TestAssess();
  }

  @Test
  @DisplayName("Auth user success test")
  void authUserSuccess() {
    ((TestInput) input).set("test");
    sut = new CommandlineInterface(input, auth, assess);
    assertDoesNotThrow(() -> sut.authUser());
  }

  @Test
  @DisplayName("Auth user fail test")
  void authUserFail() {
    ((TestInput) input).set("fail");
    sut = new CommandlineInterface(input, auth, assess);
    assertThrows(RemoteException.class, () -> sut.authUser());
  }

  @Test
  @DisplayName("Auth user quit test")
  void authUserQuit() {
    ((TestInput) input).set("q");
    sut = new CommandlineInterface(input, auth, assess);
    assertThrows(CommandlineInterface.QuitInterrupt.class,
        () -> sut.authUser());
  }

  @Test
  @DisplayName("Load course found then keep test")
  void loadCourseFoundKept() throws Exception {
    ((TestInput) input).set("k");
    sut = new CommandlineInterface(input, auth, assess);
    assertNotNull(sut.loadCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Load course found then delete test")
  void loadCourseFoundDelete() throws Exception {
    ((TestInput) input).set("d");
    sut = new CommandlineInterface(input, auth, assess);
    assertNull(sut.loadCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Load course not found test")
  void loadCourseNotFound() throws Exception {
    sut = new CommandlineInterface(input, auth, assess);
    assertNull(sut.loadCourse("foo", "bar"));
  }

  @Test
  @DisplayName("Load course fail test")
  void loadCourseFail() {
    sut = new CommandlineInterface(input, auth, assess);
    assertThrows(RemoteException.class,
        () -> sut.loadCourse(null, null));
  }

  @Test
  @DisplayName("Set course max units test")
  void setCourseMaxUnits() throws Exception {
    IUserInput input = new IncrementInput();
    sut = new CommandlineInterface(input, auth, assess);
    ICourse actual = sut.setCourse("student123", "course123");
    assertNotNull(actual);
    assertEquals(Rules.MAX_MARK_COUNT, actual.unitMarks().size());
  }

  @Test
  @DisplayName("Set course min input test")
  void setCourseMinInput() throws Exception {
    IUserInput input = new EndInput();
    sut = new CommandlineInterface(input, auth, assess);
    ICourse actual = sut.setCourse("student123", "course123");
    assertNotNull(actual);
    assertEquals(Rules.MIN_MARK_COUNT, actual.unitMarks().size());
  }

  @Test
  @DisplayName("Set course quit test")
  void setCourseQuit() {
    ((TestInput) input).set("q");
    sut = new CommandlineInterface(input, auth, assess);
    assertThrows(CommandlineInterface.QuitInterrupt.class,
        () -> sut.setCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Save course success test")
  void saveCourseSuccess() {
    ((TestInput) input).set("s");
    sut = new CommandlineInterface(input, auth, assess);
    ICourse course = generateCourse("course123");
    assertDoesNotThrow(() -> sut.saveCourse("student123", course));
  }

  @Test
  @DisplayName("Save course cancel test")
  void saveCourseCancel() {
    ((TestInput) input).set("c");
    sut = new CommandlineInterface(input, auth, assess);
    ICourse course = generateCourse("course123");
    assertDoesNotThrow(() -> sut.saveCourse("student123", course));
  }

  @Test
  @DisplayName("Save course quit test")
  void saveCourseQuit() {
    ((TestInput) input).set("q");
    sut = new CommandlineInterface(input, auth, assess);
    ICourse course = generateCourse("course123");
    assertThrows(CommandlineInterface.QuitInterrupt.class,
        () -> sut.saveCourse("student123", course));
  }

  @Test
  @DisplayName("Delete course success test")
  void deleteCourseSuccess() throws Exception {
    ((TestInput) input).set("d");
    sut = new CommandlineInterface(input, auth, assess);
    assertTrue(sut.deleteCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Delete course fail test")
  void deleteCourseFail() throws Exception {
    ((TestInput) input).set("d");
    sut = new CommandlineInterface(input, auth, assess);
    assertFalse(sut.deleteCourse("foo", "bar"));
  }

  @Test
  @DisplayName("Delete course keep test")
  void deleteCourseKeep() throws Exception {
    ((TestInput) input).set("k");
    sut = new CommandlineInterface(input, auth, assess);
    assertFalse(sut.deleteCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Delete course cancel test")
  void deleteCourseCancel() throws Exception {
    ((TestInput) input).set("c");
    sut = new CommandlineInterface(input, auth, assess);
    assertFalse(sut.deleteCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Delete course quit test")
  void deleteCourseQuit() {
    ((TestInput) input).set("q");
    sut = new CommandlineInterface(input, auth, assess);
    assertThrows(CommandlineInterface.QuitInterrupt.class,
        () -> sut.deleteCourse("student123", "course123"));
  }

  @Test
  @DisplayName("Send request success test")
  void sendRequestSuccess() throws Exception {
    sut = new CommandlineInterface(input, auth, assess);
    ICourse course = generateCourse("course123");
    assertNotNull(sut.sendRequest("student123", course));
  }

  @Test
  @DisplayName("Send request fail test")
  void sendRequestFail() {
    sut = new CommandlineInterface(input, auth, assess);
    assertThrows(RemoteException.class,
        () -> sut.sendRequest("student123", new Course("course123")));
  }

  private ICourse generateCourse(String courseId) {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(String.format("unit%02d", i), i));
    return course;
  }

  private class TestInput implements IUserInput {
    private String input;

    void set(String input) {
      this.input = input;
    }

    @Override
    public String read() {
      return input;
    }
  }

  private class IncrementInput implements IUserInput {
    int i;

    @Override
    public String read() {
      i++;
      return String.valueOf(i);
    }
  }

  private class EndInput implements IUserInput {
    private int i;
    private final int MAX = Rules.MIN_MARK_COUNT * 2;

    @Override
    public String read() {
      i++;
      return i <= MAX ? String.valueOf(i) : "e";
    }
  }

  private class TestAuth implements IAuth {

    @Override
    public void login(String user, String pass) throws RemoteException {
      String success = "test";
      if (!(user.equals(success) && pass.equals(success)))
        throw new RemoteException("Login failed");
    }
  }

  private class TestAssess implements IAssess {

    private final String STUDENT_ID = "student123";
    private final String COURSE_ID = "course123";
    private final IRequest TEST_DATA;

    TestAssess() {
      ICourse course = new Course(COURSE_ID);
      IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
          .boxed()
          .forEach(i -> course.add(
              String.format("unit%02d", i),
              Rules.PASS_MARK
          ));
      TEST_DATA = new Request(STUDENT_ID, course);
    }

    @Override
    public String assess(IRequest request) throws RemoteException {
      if (request == null)
        throw new RemoteException();
      return "Success";
    }

    @Override
    public void save(IRequest request) throws RemoteException {
      if (!request.course().id().equals(COURSE_ID))
        throw new RemoteException();
    }

    @Override
    public IRequest load(String studentId, String courseId) throws RemoteException {
      if (studentId == null && courseId == null)
        throw new RemoteException();
      if (!(studentId.equals(STUDENT_ID) && courseId.equals(COURSE_ID)))
        return null;
      return TEST_DATA;
    }

    @Override
    public void delete(String studentId, String courseId) throws RemoteException {
      if (!(studentId.equals(STUDENT_ID) && courseId.equals(COURSE_ID)))
        throw new RemoteException();
    }
  }
}
