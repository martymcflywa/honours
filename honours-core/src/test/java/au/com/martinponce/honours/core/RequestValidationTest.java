package au.com.martinponce.honours.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Request validation test")
class RequestValidationTest {

  private String legalId = "123";
  private Collection<Integer> legalMarks = IntStream.rangeClosed(1, Rules.MIN_MARKS)
      .boxed()
      .map(i -> Rules.PASS_MARK)
      .collect(Collectors.toList());

  @Test
  @DisplayName("Validate legal request test")
  void validateLegalRequest() {
    assertDoesNotThrow(() -> new Request(legalId, legalMarks));
  }

  @Test
  @DisplayName("Validate null id test")
  void validateIdNull() {
    assertThrows(NullPointerException.class, () -> new Request(null,
        legalMarks));
  }

  @Test
  @DisplayName("Validate empty id test")
  void validateIdEmpty() {
    assertThrows(IllegalArgumentException.class, () -> new Request("",
        legalMarks));
  }

  @Test
  @DisplayName("Validate null marks test")
  void validateMarksNull() {
    assertThrows(NullPointerException.class, () -> new Request(legalId, null));
  }

  @Test
  @DisplayName("Validate empty marks test")
  void validateMarksEmpty() {
    assertThrows(IllegalArgumentException.class, () -> new Request(legalId,
        new ArrayList<>()));
  }

  @Test
  @DisplayName("Validate marks less than minimum test")
  void validateMarksLessThanMin() {
    List<Integer> marks =
        IntStream.range(1, Rules.MIN_MARKS - 1)
            .boxed()
            .collect(Collectors.toList());
    assertThrows(IllegalArgumentException.class,
        () -> new Request(legalId, marks));
  }

  @Test
  @DisplayName("Validate marks greater than maximum test")
  void validateMarksGreaterThanMax() {
    List<Integer> marks = IntStream.rangeClosed(1, Rules.MAX_MARKS + 1)
        .boxed()
        .collect(Collectors.toList());
    assertThrows(IllegalArgumentException.class,
        () -> new Request(legalId, marks));
  }
}
