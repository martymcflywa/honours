package au.com.martinponce.honours.persist.hibernate;

import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntityId;
import au.com.martinponce.honours.persist.hibernate.entities.HonoursEntity;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
@DisplayName("Repository test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RepositoryTest {

  private Repository sut;

  private final String STUDENT_ID = "student123";
  private final String COURSE_ID = "course123";
  private final String UNIT_ID = "unit1";
  private final int MARK = 50;

  private final HonoursEntityId ID = new HonoursEntityId(STUDENT_ID, COURSE_ID, UNIT_ID);
  private final HonoursEntity ENTITY = new HonoursEntity(ID, MARK);
  private final List<HonoursEntity> ENTITIES = new ArrayList<HonoursEntity>() {
    {
      add(ENTITY);
    }
  };

  @BeforeAll
  @Test
  @DisplayName("SaveOrUpdate success test")
  void beforeAll() {
    Configuration configuration = TestConfig.load();
    sut = new Repository(configuration);
  }

  @Test
  @DisplayName("SaveOrUpdate success test")
  @Order(1)
  void saveOrUpdateSuccess() {
    assertDoesNotThrow(() -> sut.saveOrUpdate(ENTITIES));
  }

  @Test
  @DisplayName("Get by studentId success test")
  @Order(2)
  void getByStudentIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(STUDENT_ID);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(ENTITY));
  }

  @Test
  @DisplayName("Get by studentId and courseId success test")
  @Order(3)
  void getByStudentCourseIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(STUDENT_ID, COURSE_ID);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(ENTITY));
  }

  @Test
  @DisplayName("Get by studentId, courseId and unitId success test")
  @Order(4)
  void getByStudentCourseUnitIdSuccess() {
    Collection<HonoursEntity> actual = sut.get(
        STUDENT_ID, COURSE_ID, UNIT_ID);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(ENTITY));
  }

  @Test
  @DisplayName("Delete entities success test")
  @Order(5)
  void deleteEntitiesSuccess() {
    assertFalse(sut.get(STUDENT_ID).isEmpty());
    assertDoesNotThrow(() -> sut.delete(ENTITIES));
    assertTrue(sut.get(STUDENT_ID).isEmpty());
  }
}
