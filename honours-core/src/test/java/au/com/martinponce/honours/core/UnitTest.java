package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.IUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit test")
class UnitTest {

  @Test
  @DisplayName("Validate legal unit id test")
  void validateUnitIdLegal() {
    String expected = "123";
    IUnit sut = new Unit(expected, 1);
    assertEquals(expected, sut.id());
  }

  @Test
  @DisplayName("Validate null unit id test")
  void validateUnitIdNull() {
    assertThrows(NullPointerException.class, () -> new Unit(null, 1));
  }

  @Test
  @DisplayName("Validate empty unit id test")
  void validateUnitIdEmpty() {
    assertThrows(IllegalArgumentException.class, () -> new Unit("", 1));
  }

  @Test
  @DisplayName("Unit equality true test")
  void unitEqualityTrue() {
    String id = "123";
    IUnit a = new Unit(id, 1);
    IUnit b = new Unit(id, 1);
    assertEquals(a, b);
  }

  @Test
  @DisplayName("Unit equality false test")
  void unitEqualityFalse() {
    IUnit a = new Unit("123", 1);
    IUnit b = new Unit("321", 1);
    assertNotEquals(a, b);
  }
}
