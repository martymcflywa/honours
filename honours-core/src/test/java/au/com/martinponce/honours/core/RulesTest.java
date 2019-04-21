package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Test honours study rules")
class RulesTest {

  private final String COURSE_ID = "course123";

  @Test
  @DisplayName("Calculate mark average")
  void calculateMarkAverage() {
    Map<String, IUnit> unitMarks = IntStream.rangeClosed(1, 5)
        .boxed()
        .collect(Collectors.toMap(
            i -> String.format("unit%02d", i),
            i -> new Unit(String.format("unit%02d", i), i)
        ));
    double expected = 3;
    assertEquals(expected, Rules.average(unitMarks));
  }

  @Test
  @DisplayName("Denied rule test")
  void denied() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK - 1));
    assertEquals(Result.DENIED, Rules.apply(course));
  }

  @Test
  @DisplayName("Qualified rule test")
  void qualified() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), (int) Rules.AVG_FOR_QUALIFY));
    assertEquals(Result.QUALIFIED, Rules.apply(course));
  }

  @Test
  @DisplayName("Need assessment rule test")
  void needAssessment() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), (int) Rules.AVG_FOR_ASSESSMENT));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MAX_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));
    assertEquals(Result.NEED_ASSESSMENT, Rules.apply(course));
  }

  @Test
  @DisplayName("Need permission rule test")
  void needPermission() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), (int) Rules.AVG_FOR_PERMISSION));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));
    assertEquals(Result.NEED_PERMISSION, Rules.apply(course));
  }

  @Test
  @DisplayName("Not qualified rule test")
  void notQualified() {
    ICourse course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.TOP_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(),
            (int) Rules.AVG_FOR_PERMISSION - 1));
    IntStream.rangeClosed(Rules.TOP_COUNT + 1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(i.toString(), Rules.PASS_MARK));
    assertEquals(Result.NOT_QUALIFIED, Rules.apply(course));
  }
}
