package io.github.kaso777.steamclone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
		"debug=false",
		"logging.level.root=INFO",
		"logging.level.org.springframework=INFO"
})
@ActiveProfiles("test")
class SteamCloneBackendApplicationTests {

	@Test
	void contextLoads() {
	}
}
