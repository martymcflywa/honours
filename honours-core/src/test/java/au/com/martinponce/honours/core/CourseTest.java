package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Course test")
class CourseTest {

  @Test
  @DisplayName("Validate legal course id test")
  void validateCourseIdLegal() {
    String expected = "123";
    ICourse sut = new Course(expected);
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

    ICourse sut = new Course(courseId);
    sut.add(unitId, expectedMark);

    assertEquals(expectedMark, sut.get(unitId).mark());
    assertEquals(1, sut.markTally());
  }

  @Test
  @DisplayName("Top unit marks test")
  void topMarks() {
    Collection<IUnit> lowMarks = IntStream.rangeClosed(1, 2)
        .boxed()
        .map(i -> new Unit("unit" + i, i))
        .collect(Collectors.toList());
    Collection<IUnit> topMarks = IntStream.rangeClosed(3, Rules.TOP_COUNT + 2)
        .boxed()
        .sorted(Collections.reverseOrder())
        .map(i -> new Unit("unit" + i, i))
        .collect(Collectors.toList());

    ICourse sut = new Course("course123");
    topMarks.forEach(i -> sut.add(i.id(), i.mark()));
    lowMarks.forEach(i -> sut.add(i.id(), i.mark()));
    assertEquals(topMarks, sut.top(Rules.TOP_COUNT));
  }

  @Test
  @DisplayName("Has max units true test")
  void hasMaxUnitsTrue() {
    ICourse sut = new Course("course123");
    IntStream.rangeClosed(1, Rules.MAX_MARK_COUNT)
        .boxed()
        .forEach(i -> sut.add(i.toString(), i));
    assertTrue(sut.hasMaxUnits());
  }

  @Test
  @DisplayName("Has max units false test")
  void hasMaxUnitsFalse() {
    ICourse sut = new Course("course123");
    IntStream.rangeClosed(1, Rules.MAX_MARK_COUNT - 1)
        .boxed()
        .forEach(i -> sut.add(i.toString(), i));
    assertFalse(sut.hasMaxUnits());
  }
}
