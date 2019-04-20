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

  public HonoursEntity(
      HonoursEntityId id,
      int mark) {
    this.id = id;
    this.mark = mark;
  }

  public HonoursEntityId getId() {
    return id;
  }

  public void setId(HonoursEntityId id) {
    this.id = id;
  }

  public int getMark() {
    return mark;
  }

  public void setMark(int mark) {
    this.mark = mark;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HonoursEntity)) return false;
    HonoursEntity that = (HonoursEntity) o;
    return Objects.equals(id, that.id)
        && Objects.equals(mark, that.mark);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, mark);
  }
}