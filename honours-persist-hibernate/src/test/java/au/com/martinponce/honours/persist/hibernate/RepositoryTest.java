package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntityId;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Repository test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RepositoryTest {

  private Repository sut;
  private Configuration configuration;

  private String studentId = "student123";
  private String courseId = "course123";
  private String unitId = "unit1";
  private int attempts = 1;
  private int mark = 50;

  private HonoursEntityId id = new HonoursEntityId(
      studentId, courseId, unitId);
  private HonoursEntity entity = new HonoursEntity(
      id, attempts, mark);

  @BeforeAll
  @Test
  @DisplayName("SaveOrUpdate success test")
  void beforeAll() {
    loadConfig();
    configForTest();
    sut = new Repository(configuration);
    assertDoesNotThrow(() -> sut.saveOrUpdate(entity));
  }

  @Test
  @DisplayName("Get by studentId success test")
  void getByStudentIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(studentId);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(entity));
  }

  @Test
  @DisplayName("Get by studentId and courseId success test")
  void getByStudentCourseIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(studentId, courseId);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(entity));
  }

  @Test
  @DisplayName("Get by studentId, courseId and unitId success test")
  void getByStudentCourseUnitIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(
        studentId, courseId, unitId);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(entity));
  }

  private void loadConfig() {
    ClassLoader classLoader = Repository.class.getClassLoader();
    File file = new File(
        classLoader.getResource(Repository.CONFIG_FILENAME).getFile());
    configuration = new Configuration().configure(file);
  }

  private void configForTest() {
    configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
  }
}
