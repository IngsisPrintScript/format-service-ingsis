package com.ingsis.format;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

class FormatServiceApplicationMainTest {

  @Test
  void runsMain() {
    try {
      FormatServiceApplication.main(new String[] {});
    } catch (Throwable ignored) {
    }
  }

  @Test
  void constructsApplicationInstance() {
    FormatServiceApplication app = new FormatServiceApplication();
    assertNotNull(app);
  }

  @Test
  void classIsSpringBootApplicationAnnotated() throws Exception {
    Class<?> cls = Class.forName("com.ingsis.format.FormatServiceApplication");
    assertNotNull(
        cls.getAnnotation(org.springframework.boot.autoconfigure.SpringBootApplication.class));
  }

  @Test
  void mainRunsWithMinimalWebProfile() {
    System.setProperty("spring.main.web-application-type", "none");
    try {
      FormatServiceApplication.main(new String[] {});
    } catch (Throwable ignored) {
    } finally {
      System.clearProperty("spring.main.web-application-type");
    }
  }

  @Test
  void mainWithMockedSpringApplicationRun() {
    try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
      mocked
          .when(
              () ->
                  SpringApplication.run(
                      Mockito.eq(FormatServiceApplication.class), Mockito.<String[]>any()))
          .thenReturn(null);
      FormatServiceApplication.main(new String[] {"--spring.main.web-application-type=none"});
      mocked.verify(
          () ->
              SpringApplication.run(
                  Mockito.eq(FormatServiceApplication.class), Mockito.<String[]>any()));
    }
  }

  @Test
  void mainInvokesSpringApplicationRun_whenMocked() {
    try (org.mockito.MockedStatic<org.springframework.boot.SpringApplication> mocked =
        org.mockito.Mockito.mockStatic(org.springframework.boot.SpringApplication.class)) {
      mocked
          .when(
              () ->
                  org.springframework.boot.SpringApplication.run(
                      FormatServiceApplication.class, new String[] {}))
          .thenReturn(
              org.mockito.Mockito.mock(
                  org.springframework.context.ConfigurableApplicationContext.class));

      FormatServiceApplication.main(new String[] {});
    }
  }
}
