package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IPersist;
import au.com.martinponce.honours.interfaces.IRequest;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntityId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.stream.Collectors;

public class Persist extends UnicastRemoteObject implements IPersist {

  private IRepository repository;
  private static final Logger LOG = LoggerFactory.getLogger(Persist.class);

  public Persist(IRepository repository) throws RemoteException {
    super(0);
    this.repository = repository;
  }

  @Override
  public void put(IRequest request) throws RemoteException {
    try {
      Collection<HonoursEntity> entities = toEntities(request);
      LOG.info("Insert {} rows to the database", entities.size());
      entities.forEach(e -> LOG.debug(e.toString()));
      repository.saveOrUpdate(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public IRequest get(String studentId, String courseId) throws RemoteException {
    try {
      Collection<HonoursEntity> entities = repository.get(studentId, courseId);
      LOG.info("Retrieve {} rows from the database", entities.size());
      entities.forEach(e -> LOG.debug(e.toString()));
      return toRequests(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void delete(String studentId, String courseId) throws RemoteException {
    try {
      Collection<HonoursEntity> entities = repository.get(studentId, courseId);
      if (entities.isEmpty())
        return;
      LOG.info("Delete {} rows from the database", entities.size());
      entities.forEach(e -> LOG.debug(e.toString()));
      repository.delete(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  private Collection<HonoursEntity> toEntities(IRequest request) {
    return request.course().unitMarks().values().stream()
        .map(e -> new HonoursEntity(
            new HonoursEntityId(
                request.studentId(),
                request.course().id(),
                e.id()),
            e.mark()))
        .collect(Collectors.toList());
  }

  private IRequest toRequests(Collection<HonoursEntity> entities) {
    if (entities.isEmpty())
      return null;
    HonoursEntity first = entities.stream()
        .findFirst()
        .orElseThrow(NullPointerException::new);
    String studentId = first.studentId();
    ICourse course = new Course(first.courseId());
    entities.forEach(e -> course.add(e.unitId(), e.getMark()));
    return new Request(studentId, course);
  }
}
