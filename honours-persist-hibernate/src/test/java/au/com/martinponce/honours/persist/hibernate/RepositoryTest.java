package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntityId;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Repository test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositoryTest {

  private Repository sut;
  private Configuration configuration;

  private String studentId = "student123";
  private String courseId = "course123";
  private String unitId = "unit1";
  private int mark = 50;

  private HonoursEntityId id = new HonoursEntityId(studentId, courseId, unitId);
  private HonoursEntity entity = new HonoursEntity(id, mark);
  private List<HonoursEntity> entities = new ArrayList<HonoursEntity>() {
    {
      add(entity);
    }
  };

  @BeforeAll
  @Test
  @DisplayName("SaveOrUpdate success test")
  void beforeAll() {
    loadConfig();
    configForTest();
    sut = new Repository(configuration);
  }

  @Test
  @DisplayName("SaveOrUpdate success test")
  @Order(1)
  void saveOrUpdateSuccess() {
    assertDoesNotThrow(() -> sut.saveOrUpdate(entities));
  }

  @Test
  @DisplayName("Get by studentId success test")
  @Order(2)
  void getByStudentIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(studentId);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(entity));
  }

  @Test
  @DisplayName("Get by studentId and courseId success test")
  @Order(3)
  void getByStudentCourseIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(studentId, courseId);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(entity));
  }

  @Test
  @DisplayName("Get by studentId, courseId and unitId success test")
  @Order(4)
  void getByStudentCourseUnitIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(
        studentId, courseId, unitId);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(entity));
  }

  @Test
  @DisplayName("Delete entities success test")
  @Order(5)
  void deleteEntitiesSuccess() {
    assertFalse(sut.get(studentId).isEmpty());
    assertDoesNotThrow(() -> sut.delete(entities));
    assertTrue(sut.get(studentId).isEmpty());
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
}
