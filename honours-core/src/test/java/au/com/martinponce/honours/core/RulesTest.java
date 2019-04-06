package au.com.martinponce.honours.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesTest {

  @Test
  @DisplayName("Calculate mark average")
  void calculateMarkAverage() {
    List<Integer> marks = IntStream.rangeClosed(1, 5)
        .boxed()
        .collect(Collectors.toList());
    double expected = 3;
    assertEquals(expected, Rules.average(marks));
  }

  @Test
  @DisplayName("Top five marks")
  void topFiveMarks() {
    int limit = 5;
    List<Integer> marks = IntStream.rangeClosed(1, 10)
        .boxed()
        .collect(Collectors.toList());
    List<Integer> expected = IntStream.rangeClosed(6, 10)
        .boxed()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());
    assertEquals(expected,
        Rules.top(marks, limit).collect(Collectors.toList()));
  }
}
