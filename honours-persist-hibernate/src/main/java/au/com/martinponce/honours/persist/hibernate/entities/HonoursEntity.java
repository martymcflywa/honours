package au.com.martinponce.honours.persist.hibernate.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class HonoursEntity {

  @EmbeddedId
  private HonoursEntityId id;
  private int mark;

  public HonoursEntity() {
  }

  public HonoursEntity(HonoursEntityId id, int mark) {
    this.id = id;
    this.mark = mark;
  }

  private HonoursEntityId getId() {
    return id;
  }

  public int getMark() {
    return mark;
  }

  public String studentId() {
    return id.getStudentId();
  }

  public String courseId() {
    return id.getCourseId();
  }

  public String unitId() {
    return id.getUnitId();
  }

  @Override
  public String toString() {
    return String.format("%s %d", id.toString(), mark);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HonoursEntity)) return false;
    HonoursEntity that = (HonoursEntity) o;
    return Objects.equals(toString(), that.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getMark());
  }
}
