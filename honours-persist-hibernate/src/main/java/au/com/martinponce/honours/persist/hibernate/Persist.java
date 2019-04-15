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

  Persist(IRepository repository) throws RemoteException {
    super(0);
    this.repository = repository;
  }

  @Override
  public void put(IRequest request) throws RemoteException {
    try {
      Collection<HonoursEntity> entities = toEntities(request);
      repository.saveOrUpdate(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public IRequest get(IRequest request) throws RemoteException {
    try {
      String studentId = request.studentId();
      Collection<HonoursEntity> entities = repository.get(studentId);
      return toRequest(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public void delete(IRequest request) throws RemoteException {
    try {
      String studentId = request.studentId();
      String courseId = request.course().id();
      Collection<HonoursEntity> entities = repository.get(studentId, courseId);

      if (entities.isEmpty())
        return;

      repository.delete(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  private Collection<HonoursEntity> toEntities(IRequest request) {
    return request.course().marks().entrySet().stream()
        .map(e -> new HonoursEntity(
            new HonoursEntityId(
                request.studentId(),
                request.course().id(),
                e.getKey().id()),
            e.getValue().attempts(),
            e.getValue().mark()))
        .collect(Collectors.toList());
  }

  private IRequest toRequest(Collection<HonoursEntity> entities) {
    if (entities.isEmpty())
      return null;

    HonoursEntity first = entities.stream()
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("No entities found"));
    String studentId = first.studentId();
    ICourse course = new Course(first.courseId());
    entities.forEach(e -> course.put(e.unitId(), e.getMark()));
    return new Request(studentId, course);
  }
}
