package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntityId_;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity_;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.InputStream;
import java.util.Collection;

public class Repository implements IRepository {

  private SessionFactory factory;
  static final String CONFIG_FILENAME = "hibernate.cfg.xml";

  private static final Logger LOG = LoggerFactory.getLogger(Repository.class);

  public Repository(InputStream config) {
    this(new Configuration().addInputStream(config).configure());
  }

  Repository(Configuration config) {
    init(config);
  }

  private void init(Configuration configuration) {
    try {
      factory = configuration.buildSessionFactory();
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void saveOrUpdate(Collection<HonoursEntity> entities) {
    try (Session session = factory.openSession()) {
      session.beginTransaction();
      entities.forEach(session::saveOrUpdate);
      session.getTransaction().commit();
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Collection<HonoursEntity> get(String studentId) {
    try (Session session = factory.openSession()) {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<HonoursEntity> cq = cb.createQuery(
          HonoursEntity.class);
      Root<HonoursEntity> root = cq.from(HonoursEntity.class);
      Predicate[] predicates = new Predicate[] {
          cb.equal(
              root.get(HonoursEntity_.id)
                  .get(HonoursEntityId_.studentId),
              studentId)
      };
      return get(session, cq, root, predicates);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Collection<HonoursEntity> get(
      String studentId, String courseId) {
    try (Session session = factory.openSession()) {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<HonoursEntity> cq = cb.createQuery(
          HonoursEntity.class);
      Root<HonoursEntity> root = cq.from(HonoursEntity.class);
      Predicate[] predicates = new Predicate[] {
          cb.equal(
              root.get(HonoursEntity_.id)
                  .get(HonoursEntityId_.studentId),
              studentId),
          cb.equal(
              root.get(HonoursEntity_.id)
                  .get(HonoursEntityId_.courseId),
              courseId)
      };
      return get(session, cq, root, predicates);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public Collection<HonoursEntity> get(
      String studentId, String courseId, String unitId) {
    try (Session session = factory.openSession()) {
      CriteriaBuilder cb = session.getCriteriaBuilder();
      CriteriaQuery<HonoursEntity> cq = cb.createQuery(
          HonoursEntity.class);
      Root<HonoursEntity> root = cq.from(HonoursEntity.class);
      Predicate[] predicates = new Predicate[] {
          cb.equal(
              root.get(HonoursEntity_.id)
                  .get(HonoursEntityId_.studentId),
              studentId),
          cb.equal(
              root.get(HonoursEntity_.id)
                  .get(HonoursEntityId_.courseId),
              courseId),
          cb.equal(
              root.get(HonoursEntity_.id)
                  .get(HonoursEntityId_.unitId),
              unitId)
      };
      return get(session, cq, root, predicates);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(Collection<HonoursEntity> entities) {
    try (Session session = factory.openSession()) {
      session.beginTransaction();
      entities.forEach(session::remove);
      session.getTransaction().commit();
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RuntimeException(e);
    }
  }

  private Collection<HonoursEntity> get(
      Session session,
      CriteriaQuery<HonoursEntity> criteriaQuery,
      Root<HonoursEntity> root,
      Predicate[] predicates) {
    criteriaQuery.select(root).where(predicates);
    Query query = session.createQuery(criteriaQuery);
    //noinspection unchecked
    return query.getResultList();
  }
}
