package itsprodigi.matteocasini.steam_clone_backend.config;

import itsprodigi.matteocasini.steam_clone_backend.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }
}