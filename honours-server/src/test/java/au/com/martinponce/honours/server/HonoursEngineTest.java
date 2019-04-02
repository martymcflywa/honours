package au.com.martinponce.honours.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Honours engine tests")
class HonoursEngineTest {

  @Test
  @DisplayName("Validate id test success")
  void validateIdSuccess() {
    HonoursEngine sut = new HonoursEngine();
    try {
      sut.validateId("12345");
    } catch (Exception e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  @DisplayName("Validate id test null")
  void validateIdNull() {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(NullPointerException.class, () -> sut.validateId(null));
  }

  @Test
  @DisplayName("Validate id test empty")
  void validateIdEmpty() {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class, () -> sut.validateId(""));
  }

  @Test
  @DisplayName("Validate marks test success")
  void validateMarksSuccess() {
    List<Integer> marks = IntStream.range(0, 29).boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    try {
      sut.validateMarks(marks);
    } catch (Exception e) {
      fail("Exception should not be thrown");
    }
  }

  @Test
  @DisplayName("Validate marks test less than min")
  void validateMarksLessThanMin() {
    List<Integer> marks = IntStream.range(1, 2).boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class,
        () -> sut.validateMarks(marks));
  }

  @Test
  @DisplayName("Validate marks test greater than max")
  void validateMarksGreaterThanMax() {
    List<Integer> marks = IntStream.range(0, 30).boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class,
        () -> sut.validateMarks(marks));
  }
}
