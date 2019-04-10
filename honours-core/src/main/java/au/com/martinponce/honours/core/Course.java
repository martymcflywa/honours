package au.com.martinponce.honours.core;

import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IMark;
import au.com.martinponce.honours.interfaces.IUnit;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.stream.Collectors;

public class Course implements ICourse {

  private String id;
  private Map<IUnit, IMark> marks;

  public Course(String id) {
    this.id = validate(id);
    marks = new HashMap<>();
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public Map<IUnit, IMark> marks() {
    return Collections.unmodifiableMap(marks);
  }

  @Override
  public IMark get(IUnit key) {
    return marks.get(key);
  }

  @Override
  public void put(String unitId, int mark) {
    IUnit key = new Unit(unitId);
    if (!marks.containsKey(key)) {
      marks.put(key, new Mark(mark));
      return;
    }
    IMark existing = get(key);
    existing.addAttempt();
    existing.set(mark);
  }

  @Override
  public Map<IUnit, IMark> top(int n) {
    return new ArrayList<>(marks.entrySet()
        .stream()
        .sorted(new MarkDescendingComparator())
        .collect(Collectors.toList()))
        .subList(0, n)
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public int markTally() {
    return marks.size();
  }

  @Override
  public boolean hasMaxUnits() {
    return marks.size() >= Rules.MAX_MARKS;
  }

  private String validate(String id) {
    Validate.notEmpty(id, "Course id must not be empty");
    return id;
  }

  private class MarkDescendingComparator implements Comparator<Map.Entry<IUnit,
      IMark>> {
    @Override
    public int compare(Map.Entry<IUnit, IMark> a, Map.Entry<IUnit, IMark> b) {
      IMark x = a.getValue();
      IMark y = b.getValue();
      return y.compareTo(x);
    }
  }
}
