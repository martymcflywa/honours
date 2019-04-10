package au.com.martinponce.honours.server;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.ICourse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Honours engine assessment test")
class HonoursEngineAssessTest {

  private String studentId = "student123";
  private String courseId = "course123";

  @Test
  @DisplayName("Assess denied")
  void assessDenied() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK - 1));

    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.DENIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess qualified")
  void assessQualified() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), (int) Rules.AVG_FOR_QUALIFY));

    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.QUALIFIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess need assessment")
  void assessNeedAssessment() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.put(i.toString(), (int) Rules.AVG_FOR_ASSESSMENT));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MAX_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK));

    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.NEED_ASSESSMENT_MESSAGE));
  }

  @Test
  @DisplayName("Assess need permission")
  void assessNeedPermission() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.put(i.toString(), (int) Rules.AVG_FOR_PERMISSION));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK));

    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.NEED_PERMISSION_MESSAGE));
  }

  @Test
  @DisplayName("Assess not qualified")
  void assessNotQualified() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.put(i.toString(),
            (int) Rules.AVG_FOR_PERMISSION - 1));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK));

    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(new Request(studentId, course))
        .contains(HonoursEngine.NOT_QUALIFIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess null studentId")
  void assessNullId() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), i));
    HonoursEngine sut = new HonoursEngine();
    assertThrows(NullPointerException.class,
        () -> sut.assess(new Request(null, course)));
  }

  @Test
  @DisplayName("Assess empty studentId")
  void assessEmptyId() throws Exception {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), i));
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class,
        () -> sut.assess(new Request("", course)));
  }

  @Test
  @DisplayName("Assess null marks")
  void assessNullMarks() throws Exception {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(NullPointerException.class,
        () -> sut.assess(new Request(studentId, null)));
  }

  @Test
  @DisplayName("Assess empty marks")
  void assessEmptyMarks() throws Exception {
    ICourse course = new Course(courseId);
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class,
        () -> sut.assess(new Request(studentId, course)));
  }
}
