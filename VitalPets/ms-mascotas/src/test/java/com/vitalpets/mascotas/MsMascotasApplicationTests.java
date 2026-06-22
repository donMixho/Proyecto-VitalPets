package com.vitalpets.mascotas;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MsMascotasApplicationTests {

	@Test
	@Disabled("Requiere MySQL activo — usar docker-compose up primero")
	void contextLoads() {
	}

}
