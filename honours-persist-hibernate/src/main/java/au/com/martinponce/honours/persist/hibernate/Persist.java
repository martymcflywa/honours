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
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

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
      repository.saveOrUpdate(entities);
    } catch (Exception e) {
      LOG.error("Exception", e);
      throw new RemoteException(e.getMessage());
    }
  }

  @Override
  public Collection<IRequest> get(IRequest request) throws RemoteException {
    try {
      String studentId = request.studentId();
      Collection<HonoursEntity> entities = repository.get(studentId);
      return toRequests(entities);
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
    return request.course().unitMarks().stream()
        .map(e -> new HonoursEntity(
            new HonoursEntityId(
                request.studentId(),
                request.course().id(),
                e.id()),
            e.mark()))
        .collect(Collectors.toList());
  }

  private Collection<IRequest> toRequests(Collection<HonoursEntity> entities) {
    if (entities.isEmpty())
      return null;

    Set<Map.Entry<String, List<HonoursEntity>>> groupedByCourseId = entities
        .stream()
        .collect(groupingBy(HonoursEntity::courseId))
        .entrySet();

    Collection<IRequest> requests = new ArrayList<>();

    for (Map.Entry<String, List<HonoursEntity>> entry : groupedByCourseId) {
      List<HonoursEntity> list = entry.getValue();
      HonoursEntity first = list.stream()
          .findFirst()
          .orElseThrow(
              () -> new NullPointerException("Empty entities collection"));
      String studentId = first.studentId();
      String courseId = first.courseId();
      ICourse course = new Course(courseId);
      list.forEach(i -> course.add(i.unitId(), i.getMark()));
      requests.add(new Request(studentId, course));
    }
    return requests;
  }
}
