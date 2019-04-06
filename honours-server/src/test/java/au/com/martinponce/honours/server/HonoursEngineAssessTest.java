package au.com.martinponce.honours.server;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Honours engine assessment test")
class HonoursEngineAssessTest {

  private String id = "12345";

  @Test
  @DisplayName("Calculate mark average")
  void calculateMarkAverage() throws Exception {
    List<Integer> marks = IntStream.rangeClosed(1, 5)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    double expected = 3;
    assertEquals(expected, sut.average(marks));
  }

  @Test
  @DisplayName("Top five marks")
  void topFiveMarks() throws Exception {
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
  void assessDenied() throws Exception {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MIN_MARKS)
        .boxed()
        .map(i -> HonoursEngine.PASS_MARK - 1)
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(id, marks).contains(HonoursEngine.DENIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess qualified")
  void assessQualified() throws Exception {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MIN_MARKS)
        .boxed()
        .map(i -> (int) HonoursEngine.AVG_FOR_QUALIFY)
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertTrue(sut.assess(id, marks).contains(HonoursEngine.QUALIFIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess need assessment")
  void assessNeedAssessment() throws Exception {
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
    assertTrue(
        sut.assess(id, marks).contains(HonoursEngine.NEED_ASSESSMENT_MESSAGE));
  }

  @Test
  @DisplayName("Assess need permission")
  void assessNeedPermission() throws Exception {
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
    assertTrue(
        sut.assess(id, marks).contains(HonoursEngine.NEED_PERMISSION_MESSAGE));
  }

  @Test
  @DisplayName("Assess not qualified")
  void assessNotQualified() throws Exception {
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
    assertTrue(
        sut.assess(id, marks).contains(HonoursEngine.NOT_QUALIFIED_MESSAGE));
  }

  @Test
  @DisplayName("Assess null id")
  void assessNullId() throws Exception {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MIN_MARKS)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertThrows(RemoteException.class, () -> sut.assess(null, marks));
  }

  @Test
  @DisplayName("Assess empty id")
  void assessEmptyId() throws Exception {
    List<Integer> marks = IntStream.rangeClosed(1, HonoursEngine.MIN_MARKS)
        .boxed()
        .collect(Collectors.toList());
    HonoursEngine sut = new HonoursEngine();
    assertThrows(RemoteException.class, () -> sut.assess("", marks));
  }

  @Test
  @DisplayName("Assess null marks")
  void assessNullMarks() throws Exception {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(RemoteException.class, () -> sut.assess(id, null));
  }

  @Test
  @DisplayName("Assess empty marks")
  void assessEmptyMarks() throws Exception {
    HonoursEngine sut = new HonoursEngine();
    assertThrows(RemoteException.class, () -> sut.assess(id,
        new ArrayList<>()));
  }
}
