package au.com.martinponce.honours.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Honours engine assessment test")
class HonoursEngineAssessTest {

  @Test
  @DisplayName("Disqualified with too many fails")
  void isDisqualifiedTooManyFails() {
    HonoursEngine sut = new HonoursEngine();
    List<Integer> marks = IntStream.range(1, HonoursEngine.MIN_MARKS)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK - 1)
        .collect(Collectors.toList());
    assertTrue(sut.isDisqualified(marks));
  }

  @Test
  @DisplayName("Not disqualified with only one fail")
  void notDisqualifiedOneFail() {
    HonoursEngine sut = new HonoursEngine();
    List<Integer> marks = IntStream.range(1, HonoursEngine.MIN_MARKS)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK)
        .collect(Collectors.toList());
    marks.add(HonoursEngine.PASS_MARK - 1);
    assertFalse(sut.isDisqualified(marks));
  }

  @Test
  @DisplayName("Calculate mark average")
  void calculateMarkAverage() {
    List<Integer> marks = IntStream.rangeClosed(1, 5)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    double expected = 3;
    assertEquals(expected, sut.average(marks));
  }

  @Test
  @DisplayName("Top five marks")
  void topFiveMarks() {
    int limit = 5;
    List<Integer> marks = IntStream.rangeClosed(1, 10)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    List<Integer> expected = IntStream.rangeClosed(6, 10)
        .boxed()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
    assertEquals(expected, sut.top(marks, limit).collect(Collectors.toList()));
  }
}
