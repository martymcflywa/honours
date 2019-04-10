package au.com.martinponce.honours.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit test")
class UnitTest {

  @Test
  @DisplayName("Validate legal unit id test")
  void validateUnitIdLegal() {
    String expected = "123";
    Unit sut = new Unit(expected);
    assertEquals(expected, sut.id());
  }

  @Test
  @DisplayName("Validate null unit id test")
  void validateUnitIdNull() {
    assertThrows(NullPointerException.class, () -> new Unit(null));
  }

  @Test
  @DisplayName("Validate empty unit id test")
  void validateUnitIdEmpty() {
    assertThrows(IllegalArgumentException.class, () -> new Unit(""));
  }

  @Test
  @DisplayName("Unit equality true test")
  void unitEqualityTrue() {
    String id = "123";
    Unit a = new Unit(id);
    Unit b = new Unit(id);
    assertTrue(a.equals(b));
  }

  @Test
  @DisplayName("Unit equality false test")
  void unitEqualityFalse() {
    Unit a = new Unit("123");
    Unit b = new Unit("321");
    assertFalse(a.equals(b));
  }
}
