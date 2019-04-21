package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IRequest;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Persist test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersistTest {

  private Configuration configuration;
  private IRepository repository;
  private IRepository failingRepository;

  private final String STUDENT_ID = "student123";
  private final String COURSE_ID = "course123";
  private ICourse course;
  private IRequest expected;

  @BeforeAll
  void beforeAll() {
    loadConfig();
    configForTest();
    course = initCourse();
    expected = new Request(STUDENT_ID, course);
    repository = new Repository(configuration);
    failingRepository = new FailingRepository();
  }

  @Test
  @DisplayName("Put success test")
  @Order(1)
  void putSuccess() throws Exception {
    Persist sut = new Persist(repository);
    assertDoesNotThrow(() -> sut.put(expected));
  }

  @Test
  @DisplayName("Put fail test")
  @Order(2)
  void putFail() throws Exception {
    Persist sut = new Persist(failingRepository);
    assertThrows(RemoteException.class, () -> sut.put(expected));
  }

  @Test
  @DisplayName("Get success test")
  @Order(3)
  void getSuccess() throws Exception {
    Persist sut = new Persist(repository);
    IRequest actual = sut.get(STUDENT_ID, COURSE_ID);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Get fail test")
  @Order(4)
  void getFail() throws Exception {
    Persist sut = new Persist(failingRepository);
    assertThrows(RemoteException.class, () -> sut.get(STUDENT_ID, COURSE_ID));
  }

  @Test
  @DisplayName("Delete success test")
  @Order(5)
  void deleteSuccess() throws Exception {
    Persist sut = new Persist(repository);
    IRequest actual = sut.get(STUDENT_ID, COURSE_ID);
    assertNotNull(actual);
    assertDoesNotThrow(() -> sut.delete(STUDENT_ID, COURSE_ID));
    assertNull(sut.get(STUDENT_ID, COURSE_ID));
  }

  @Test
  @DisplayName("Delete fail test")
  void deleteFail() throws Exception {
    Persist sut = new Persist(failingRepository);
    assertThrows(RemoteException.class,
        () -> sut.delete(STUDENT_ID, COURSE_ID));
  }

  private void loadConfig() {
    ClassLoader classLoader = Repository.class.getClassLoader();
    File file = new File(
        Objects.requireNonNull(
            classLoader.getResource(Repository.CONFIG_FILENAME)).getFile());
    configuration = new Configuration().configure(file);
  }

  private void configForTest() {
    configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
  }

  private ICourse initCourse() {
    course = new Course(COURSE_ID);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(
            String.format("unit%02d", i),
            Rules.PASS_MARK));
    return course;
  }

  private class FailingRepository implements IRepository {

    @Override
    public void saveOrUpdate(Collection<HonoursEntity> entities) {
      throw new RuntimeException("SaveOrUpdate not implemented");
    }

    @Override
    public Collection<HonoursEntity> get(String studentId) {
      throw new RuntimeException("Get not implemented");
    }

    @Override
    public Collection<HonoursEntity> get(String studentId, String courseId) {
      throw new RuntimeException("Get not implemented");
    }

    @Override
    public Collection<HonoursEntity> get(
        String studentId, String courseId, String unitId) {
      throw new RuntimeException("Get not implemented");
    }

    @Override
    public void delete(Collection<HonoursEntity> entities) {
      throw new RuntimeException("Delete not implemented");
    }
  }
}
