package au.com.martinponce.honours.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Honours engine assessment test")
class HonoursEngineAssessTest {

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

  @Test
  @DisplayName("Assess denied")
  void assessDenied() {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MAX_FAILS + 1)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK - 1)
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertEquals(Result.DENIED, sut.assess(marks));
  }

  @Test
  @DisplayName("Assess qualified")
  void assessQualified() {
    List<Integer> marks = IntStream.rangeClosed(1, 3)
        .boxed()
        .map(i -> (int) HonoursEngine.AVG_FOR_QUALIFY)
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertEquals(Result.QUALIFIED, sut.assess(marks));
  }

  @Test
  @DisplayName("Assess need assessment")
  void assessNeedAssessment() {
    List<Integer> topMarks = IntStream.rangeClosed(1, 8)
        .boxed()
        .map(i -> (int) HonoursEngine.AVG_FOR_ASSESSMENT)
        .collect(Collectors.toList());
    List<Integer> marks = IntStream.rangeClosed(1, 22)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK)
        .collect(Collectors.toList());
    marks.addAll(topMarks);
    HonoursEngine sut = new HonoursEngine();
    assertEquals(Result.NEED_ASSESSMENT, sut.assess(marks));
  }

  @Test
  @DisplayName("Assess need permission")
  void assessNeedPermission() {
    List<Integer> topMarks = IntStream.rangeClosed(1, 8)
        .boxed()
        .map(i -> (int) HonoursEngine.AVG_FOR_PERMISSION)
        .collect(Collectors.toList());
    List<Integer> marks = IntStream.rangeClosed(1, 22)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK)
        .collect(Collectors.toList());
    marks.addAll(topMarks);
    HonoursEngine sut = new HonoursEngine();
    assertEquals(Result.NEED_PERMISSION, sut.assess(marks));
  }

  @Test
  @DisplayName("Assess need permission")
  void assessNotQualified() {
    List<Integer> topMarks = IntStream.rangeClosed(1, 8)
        .boxed()
        .map(i -> (int) HonoursEngine.AVG_FOR_PERMISSION - 1)
        .collect(Collectors.toList());
    List<Integer> marks = IntStream.rangeClosed(1, 22)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK)
        .collect(Collectors.toList());
    marks.addAll(topMarks);
    HonoursEngine sut = new HonoursEngine();
    assertEquals(Result.NOT_QUALIFIED, sut.assess(marks));
  }
}
