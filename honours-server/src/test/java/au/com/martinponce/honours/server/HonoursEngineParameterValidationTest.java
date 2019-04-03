package au.com.martinponce.honours.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Honours engine parameter validation test")
class HonoursEngineParameterValidationTest {

  @Test
  @DisplayName("Validate legal id test")
  void validateIdSuccess() {
    HonoursEngine sut = new HonoursEngine();
    assertDoesNotThrow(() -> sut.validateId("12345"));
  }

  @Test
  @DisplayName("Validate null id test")
  void validateIdNull() {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(NullPointerException.class, () -> sut.validateId(null));
  }

  @Test
  @DisplayName("Validate empty id test")
  void validateIdEmpty() {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class, () -> sut.validateId(""));
  }

  @Test
  @DisplayName("Validate legal marks test")
  void validateMarksSuccess() {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MAX_MARKS)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertDoesNotThrow(() -> sut.validateMarks(marks));
  }

  @Test
  @DisplayName("Validate marks less than minimum test")
  void validateMarksLessThanMin() {
    List<Integer> marks =
        IntStream.range(1, HonoursEngine.MIN_MARKS - 1)
            .boxed()
            .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class,
        () -> sut.validateMarks(marks));
  }

  @Test
  @DisplayName("Validate marks greater than maximum test")
  void validateMarksGreaterThanMax() {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MAX_MARKS + 1)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertThrows(IllegalArgumentException.class,
        () -> sut.validateMarks(marks));
  }
}
