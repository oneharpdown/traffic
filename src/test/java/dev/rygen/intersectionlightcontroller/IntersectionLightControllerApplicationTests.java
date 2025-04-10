package dev.rygen.intersectionlightcontroller;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IntersectionLightControllerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void roadALightIsRedWhenRoadBLightIsSetToGreen() {
		var testIntersection = new Intersection();
		testIntersection.setRoadBLightColor(LightStatus.green);
		assert testIntersection.getRoadALightColor().equals(LightStatus.red);
	}

	@Test
	void roadBLightIsRedWhenRoadALightIsSetToGreen() {
		var testIntersection = new Intersection();
		testIntersection.setRoadALightColor(LightStatus.green);
		assert testIntersection.getRoadBLightColor().equals(LightStatus.red);
	}
	

}
