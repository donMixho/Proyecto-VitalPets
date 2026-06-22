package com.vitalpets.mascotas;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Requiere MySQL activo — usar docker-compose up primero")
@SpringBootTest
class MsMascotasApplicationTests {

	@Test
	void contextLoads() {
	}

}
