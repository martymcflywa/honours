package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.stream.Collectors;

public class Course implements ICourse {

  private final String ID;
  private final Collection<IUnit> UNIT_MARKS;

  public Course(String id) {
    ID = validate(id);
    UNIT_MARKS = new ArrayList<>();
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public Collection<IUnit> unitMarks() {
    return Collections.unmodifiableCollection(UNIT_MARKS);
  }

  @Override
  public IUnit get(String unitId) {
    return UNIT_MARKS.stream()
        .filter(u -> u.id().equals(unitId))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void add(String unitId, int mark) {
    Unit unit = new Unit(unitId, mark);
    UNIT_MARKS.add(unit);
  }

  @Override
  public Collection<IUnit> top(int n) {
    return UNIT_MARKS.stream()
        .sorted(new MarkDescendingComparator())
        .collect(Collectors.toList())
        .subList(0, n);
  }

  @Override
  public int markTally() {
    return UNIT_MARKS.size();
  }

  @Override
  public boolean hasMaxUnits() {
    return UNIT_MARKS.size() >= Rules.MAX_MARK_COUNT;
  }

  private String validate(String id) {
    Validate.notEmpty(id, "Course id must not be empty");
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof  Course)) return false;
    Course that = (Course) o;
    return Objects.equals(id(), that.id())
        && Objects.equals(
        UNIT_MARKS.stream()
            .sorted(new MarkDescendingComparator())
            .collect(Collectors.toList()),
        UNIT_MARKS.stream()
            .sorted(new MarkDescendingComparator())
            .collect(Collectors.toList()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id(), unitMarks());
  }

  private class MarkDescendingComparator implements Comparator<IUnit> {
    @Override
    public int compare(IUnit a, IUnit b) {
      return b.compareTo(a);
    }
  }
}
