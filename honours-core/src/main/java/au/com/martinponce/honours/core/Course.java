package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.stream.Collectors;

public class Course implements ICourse {

  private final String ID;
  private final Map<String, IUnit> UNIT_MARKS;

  public Course(String id) {
    ID = validate(id);
    UNIT_MARKS = new HashMap<>();
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public Map<String, IUnit> unitMarks() {
    return Collections.unmodifiableMap(UNIT_MARKS);
  }

  @Override
  public IUnit get(String unitId) {
    return UNIT_MARKS.get(unitId);
  }

  @Override
  public void add(String unitId, int mark) {
    Unit unit = new Unit(unitId, mark);
    UNIT_MARKS.put(unitId, unit);
  }

  @Override
  public Map<String, IUnit> top(int n) {
    return new ArrayList<>(UNIT_MARKS.entrySet()
        .stream()
        .sorted(new MarkDescendingComparator())
        .collect(Collectors.toList()))
        .subList(0, n)
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
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
        && Objects.equals(unitMarks(), that.unitMarks());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id(), unitMarks());
  }

  private class MarkDescendingComparator implements Comparator<Map.Entry<String, IUnit>> {

    @Override
    public int compare(Map.Entry<String, IUnit> a, Map.Entry<String, IUnit> b) {
      return b.getValue().compareTo(a.getValue());
    }
  }
}
