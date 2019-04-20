package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.core.Course;
import au.com.martinponce.honours.core.Request;
import au.com.martinponce.honours.core.Rules;
import au.com.martinponce.honours.interfaces.ICourse;
import au.com.martinponce.honours.interfaces.IPersist;
import au.com.martinponce.honours.interfaces.IRequest;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Persist test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersistTest {

  private Configuration configuration;
  private IPersist sut;

  private String studentId = "student123";
  private String courseId = "course123";
  private ICourse course;
  private IRequest expected;

  @BeforeAll
  void beforeAll() throws Exception {
    loadConfig();
    configForTest();
    course = initCourse(courseId);
    expected = new Request(studentId, course);
    IRepository repository = new Repository(configuration);
    sut = new Persist(repository);
  }

  @Test
  @DisplayName("Put success test")
  @Order(1)
  void putSuccess() {
    assertDoesNotThrow(() -> sut.put(expected));
  }

  @Test
  @DisplayName("Get success test")
  @Order(2)
  void getSuccess() throws Exception {
    IRequest actual = sut.get(studentId, courseId);
    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Delete success test")
  @Order(3)
  void deleteSuccess() throws Exception {
    IRequest actual = sut.get(studentId, courseId);
    assertNotNull(actual);
    assertDoesNotThrow(() -> sut.delete(studentId, courseId));
    assertNull(sut.get(studentId, courseId));
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

  private ICourse initCourse(String courseId) {
    course = new Course(courseId);
    IntStream.rangeClosed(1, Rules.MIN_MARK_COUNT)
        .boxed()
        .forEach(i -> course.add(
            String.format("unit%02d", i),
            Rules.PASS_MARK));
    return course;
  }
}
