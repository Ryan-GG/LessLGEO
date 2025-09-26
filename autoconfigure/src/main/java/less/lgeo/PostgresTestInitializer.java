package less.lgeo;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

/**
 * Starts up instance of PgSQL for integration tests
 */
public class PostgresTestInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext>, AfterAllCallback {

  private static final String IMAGE_NAME = "postgres";
  private static final String TAG = "17.5";
  public static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse(IMAGE_NAME)
      .withTag(TAG);
  private static final Logger logger = LoggerFactory.getLogger(PostgresTestInitializer.class);
  private static final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
  private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
      POSTGRES_IMAGE)
      .withLogConsumer(logConsumer)
      .withDatabaseName("postgres")
      .withUsername("postgres")
      .withPassword("postgres");

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    postgreSQLContainer.start();
    TestPropertyValues.of(
        "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
        "spring.datasource.username=" + postgreSQLContainer.getUsername(),
        "spring.datasource.password=" + postgreSQLContainer.getPassword()
    ).applyTo(applicationContext.getEnvironment());
  }

  @Override
  public void afterAll(ExtensionContext context) {
    if (postgreSQLContainer == null) {
      return;
    }
    postgreSQLContainer.close();
  }
}
