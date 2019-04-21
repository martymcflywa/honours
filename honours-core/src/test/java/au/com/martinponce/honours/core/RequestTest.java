package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Request test")
class RequestTest {

  private final String STUDENT_ID = "student123";
  private final String COURSE_ID = "course123";
  private ICourse course;

  @BeforeAll
  void beforeAll() {
    course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));
  }

  @Test
  @DisplayName("Validate legal request test")
  void validateLegalRequest() {
    assertDoesNotThrow(() -> new Request(STUDENT_ID, course));
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
  @DisplayName("Validate null unitMarks test")
  void validateMarksNull() {
    assertThrows(NullPointerException.class,
        () -> new Request(STUDENT_ID, null));
  }

  @Test
  @DisplayName("Validate empty unitMarks test")
  void validateMarksEmpty() {
    assertThrows(IllegalArgumentException.class,
        () -> new Request(STUDENT_ID, new Course(COURSE_ID)));
  }

  @Test
  @DisplayName("Validate unitMarks less than minimum test")
  void validateMarksLessThanMin() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT - 1)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));
    assertThrows(IllegalArgumentException.class,
        () -> new Request(STUDENT_ID, course));
  }

  @Test
  @DisplayName("Validate unitMarks greater than maximum test")
  void validateMarksGreaterThanMax() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MAX_MARK_COUNT + 1)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));
    assertThrows(IllegalArgumentException.class,
        () -> new Request(STUDENT_ID, course));
  }

  @Test
  @DisplayName("Equals true test")
  void equalsTrue() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(String.format("unit%02d", i), Rules.PASS_MARK));
    IRequest a = new Request(STUDENT_ID, course);
    IRequest b = new Request(STUDENT_ID, course);
    assertEquals(a, b);
  }

  @Test
  @DisplayName("Equals false test")
  void equalsFalse() {
    ICourse courseA = new Course("course123");
    ICourse courseB = new Course("course321");
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> {
          courseA.add(String.format("unit%02d", i), Rules.PASS_MARK);
          courseB.add(String.format("unit%03d", i), Rules.PASS_MARK + 1);
        });
    IRequest a = new Request("student123", courseA);
    IRequest b = new Request("student321", courseA);
    assertNotEquals(a, b);
  }
}
