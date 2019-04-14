package au.com.martinponce.honours.persist.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HonoursEntityId implements Serializable {

  @Column
  private String studentId;
  @Column
  private String courseId;
  @Column
  private String unitId;

  public HonoursEntityId() {
  }

  public HonoursEntityId(
      String studentId, String courseId, String unitId) {
    this.studentId = studentId;
    this.courseId = courseId;
    this.unitId = unitId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public String getCourseId() {
    return courseId;
  }

  public void setCourseId(String courseId) {
    this.courseId = courseId;
  }

  public String getUnitId() {
    return unitId;
  }

  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof HonoursEntityId)) return false;
    HonoursEntityId that = (HonoursEntityId) o;
    return Objects.equals(studentId, that.studentId)
        && Objects.equals(courseId, that.courseId)
        && Objects.equals(unitId, that.unitId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId, unitId);
  }
}
