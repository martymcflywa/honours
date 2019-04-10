package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IMark;
import au.com.martinponce.honours.interfaces.IUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course test")
class CourseTest {

  @Test
  @DisplayName("Validate legal course id test")
  void validateCourseIdLegal() {
    String expected = "123";
    Course sut = new Course(expected);
    assertEquals(expected, sut.id());
  }

  @Test
  @DisplayName("Validate null course id test")
  void validateCourseIdNull() {
    assertThrows(NullPointerException.class, () -> new Course(null));
  }

  @Test
  @DisplayName("Validate empty course id test")
  void validateCourseIdEmpty() {
    assertThrows(IllegalArgumentException.class, () -> new Course(""));
  }

  @Test
  @DisplayName("Add new unit mark test")
  void addNewUnitMark() {
    String courseId = "course123";
    String unitId = "unit123";
    int expectedMark = 50;
    int expectedAttempts = 1;

    Course sut = new Course(courseId);
    sut.put(unitId, expectedMark);

    assertEquals(expectedMark, sut.get(new Unit(unitId)).mark());
    assertEquals(expectedAttempts, sut.get(new Unit(unitId)).attempts());
    assertEquals(1, sut.marks().size());
  }

  @Test
  @DisplayName("Top marks test")
  void topMarks() {
    List<Integer> lowMarks = IntStream.rangeClosed(1, 2)
        .boxed()
        .collect(Collectors.toList());
    List<Integer> topMarks = IntStream.rangeClosed(3, Rules.TOP_COUNT + 2)
        .boxed()
        .collect(Collectors.toList());
    Map<IUnit, IMark> expected = topMarks.stream()
        .collect(Collectors.toMap(
            i -> new Unit(i.toString()),
            Mark::new));

    ICourse sut = new Course("course123");
    topMarks.forEach(i -> sut.put(i.toString(), i));
    lowMarks.forEach(i -> sut.put(i.toString(), i));

    assertEquals(expected, sut.top(Rules.TOP_COUNT));
  }

  @Test
  @DisplayName("Has max units true test")
  void hasMaxUnitsTrue() {
    ICourse sut = new Course("course123");
    IntStream.rangeClosed(1, Rules.MAX_MARKS)
        .boxed()
        .forEach(i -> sut.put(i.toString(), i));
    assertTrue(sut.hasMaxUnits());
  }

  @Test
  @DisplayName("Has max units false test")
  void hasMaxUnitsFalse() {
    ICourse sut = new Course("course123");
    IntStream.rangeClosed(1, Rules.MAX_MARKS - 1)
        .boxed()
        .forEach(i -> sut.put(i.toString(), i));
    assertFalse(sut.hasMaxUnits());
  }
}
