package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class Course implements ICourse {

  private String id;
  private Collection<IUnit> unitMarks;

  public Course(String id) {
    this.id = validate(id);
    unitMarks = new ArrayList<>();
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public Collection<IUnit> unitMarks() {
    return Collections.unmodifiableCollection(unitMarks);
  }

  @Override
  public IUnit get(String unitId) {
    return unitMarks.stream()
        .filter(u -> u.id().equals(unitId))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void add(String unitId, int mark) {
    Unit unit = new Unit(unitId, mark);
    unitMarks.add(unit);
  }

  @Override
  public Collection<IUnit> top(int n) {
    return unitMarks.stream()
        .sorted(new MarkDescendingComparator())
        .collect(Collectors.toList())
        .subList(0, n);
  }

  @Override
  public int markTally() {
    return unitMarks.size();
  }

  @Override
  public boolean hasMaxUnits() {
    return unitMarks.size() >= Rules.MAX_MARK_COUNT;
  }

  private String validate(String id) {
    Validate.notEmpty(id, "Course id must not be empty");
    return id;
  }

  private class MarkDescendingComparator implements Comparator<IUnit> {
    @Override
    public int compare(IUnit a, IUnit b) {
      return b.compareTo(a);
    }
  }
}
