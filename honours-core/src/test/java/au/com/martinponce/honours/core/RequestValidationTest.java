package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Request validation test")
class RequestValidationTest {

  private String studentId = "student123";
  private String courseId = "course123";
  private ICourse course;

  @BeforeAll
  void beforeAll() {
    course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARKS)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK));
  }

  @Test
  @DisplayName("Validate legal request test")
  void validateLegalRequest() {
    assertDoesNotThrow(() -> new Request(studentId, course));
  }

  @Test
  @DisplayName("Validate null id test")
  void validateIdNull() {
    assertThrows(NullPointerException.class,
        () -> new Request(null, course));
  }

  @Test
  @DisplayName("Validate empty id test")
  void validateIdEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> new Request("", course));
  }

  @Test
  @DisplayName("Validate null marks test")
  void validateMarksNull() {
    assertThrows(NullPointerException.class,
        () -> new Request(studentId, null));
  }

  @Test
  @DisplayName("Validate empty marks test")
  void validateMarksEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> new Request(studentId, new Course(courseId)));
  }

  @Test
  @DisplayName("Validate marks less than minimum test")
  void validateMarksLessThanMin() {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARKS - 1)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK));
    assertThrows(IllegalArgumentException.class,
        () -> new Request(studentId, course));
  }

  @Test
  @DisplayName("Validate marks greater than maximum test")
  void validateMarksGreaterThanMax() {
    ICourse course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MAX_MARKS + 1)
        .boxed()
        .forEach(i -> course.put(i.toString(), Rules.PASS_MARK));
    assertThrows(IllegalArgumentException.class,
        () -> new Request(studentId, course));
  }
}
