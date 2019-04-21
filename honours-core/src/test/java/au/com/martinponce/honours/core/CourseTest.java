package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
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
        .map(i -> new Unit(String.format("unit%02d", i), i))
        .collect(Collectors.toList());
    Collection<IUnit> topMarks = IntStream.rangeClosed(3, Rules.TOP_COUNT + 2)
        .boxed()
        .sorted(Collections.reverseOrder())
        .map(i -> new Unit(String.format("unit%02d", i), i))
        .collect(Collectors.toList());
    Map<String, IUnit> expected = topMarks.stream()
        .collect(Collectors.toMap(
            IUnit::id,
            i -> new Unit(i.id(), i.mark())));

    ICourse sut = new Course("course123");
    topMarks.forEach(i -> sut.add(i.id(), i.mark()));
    lowMarks.forEach(i -> sut.add(i.id(), i.mark()));
    assertEquals(expected, sut.top(Rules.TOP_COUNT));
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

  @Test
  @DisplayName("Equals true test")
  void equalsTrue() {
    String courseId = "course123";
    ICourse a = new Course(courseId);
    ICourse b = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> {
          String unitId = String.format("unit%02d", i);
          a.add(unitId, Rules.PASS_MARK);
          b.add(unitId, Rules.PASS_MARK);
        });
    assertEquals(a, b);
  }

  @Test
  @DisplayName("Equals false test")
  void equalsFalse() {
    ICourse a = new Course("course123");
    ICourse b = new Course("course321");
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> {
          String unitId = String.format("unit%02d", i);
          a.add(unitId, Rules.PASS_MARK);
          b.add(unitId, Rules.PASS_MARK - 1);
        });
    assertNotEquals(a, b);
  }
}
