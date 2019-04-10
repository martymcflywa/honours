package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.IMark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mark test")
class MarkTest {

  @Test
  @DisplayName("Validate legal mark test")
  void validateLegalMark() {
    int expected = 1;
    IMark sut = new Mark(expected);
    assertEquals(expected, sut.mark());
  }

  @Test
  @DisplayName("Validate mark less than minimum test")
  void validateMarkLessThanMin() {
    assertThrows(IllegalArgumentException.class, () -> new Mark(-1));
  }

  @Test
  @DisplayName("Validate mark greater than maximum test")
  void validateMarkGreaterThanMax() {
    assertThrows(IllegalArgumentException.class, () -> new Mark(101));
  }

  @Test
  @DisplayName("Compare less than test")
  void compareLessThan() {
    IMark a = new Mark(1);
    IMark b = new Mark(2);
    assertEquals(-1, a.compareTo(b));
  }

  @Test
  @DisplayName("Compare more than test")
  void compareMoreThan() {
    IMark a = new Mark(2);
    IMark b = new Mark(1);
    assertEquals(1, a.compareTo(b));
  }

  @Test
  @DisplayName("Compare equal test")
  void compareEqual() {
    IMark a = new Mark(1);
    IMark b = new Mark(1);
    assertEquals(0, a.compareTo(b));
  }

  @Test
  @DisplayName("Equality true test")
  void equalityTrue() {
    IMark a = new Mark(1);
    IMark b = new Mark(1);
    assertTrue(a.equals(b));
  }

  @Test
  @DisplayName("Equality false test")
  void equalityFalse() {
    IMark a = new Mark(1);
    IMark b = new Mark(2);
    assertFalse(a.equals(b));
  }
}
