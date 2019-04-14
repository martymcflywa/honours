package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;

import java.util.Collection;

public interface IRepository {
  void saveOrUpdate(HonoursEntity entity);
  Collection<HonoursEntity> get(String studentId);
  Collection<HonoursEntity> get(String studentId, String courseId);
  Collection<HonoursEntity> get(
      String studentId, String courseId, String unitId);
}
