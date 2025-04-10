package dev.rygen.intersectionlightcontroller;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntersectionTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private IntersectionRepository intersectionRepository;

	@BeforeEach
	public void setup() {
		// Clean up the repository before each test
		intersectionRepository.deleteAll();
	}

	@Test
	public void testCreateIntersection() {
		Intersection intersection = new Intersection();
		intersection.setRoadALightColor(LightStatus.red);
		intersection.setRoadBLightColor(LightStatus.green);

		// Act
		ResponseEntity<Intersection> response = restTemplate.postForEntity(
				"/api/intersections",
				intersection,
				Intersection.class);

		// Assert
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());


		// Verify it's in the database
		Intersection savedIntersection = intersectionRepository.findById(response.getBody().getIntersectionId()).orElse(null);
		assertNotNull(savedIntersection);
	}

	@Test
	public void testTurnOffAllLights() {
		// Arrange - Create and save an intersection
		Intersection intersection = new Intersection();
		intersection.setRoadALightColor(LightStatus.green);
		intersection.setRoadBLightColor(LightStatus.red);

		Intersection savedIntersection = intersectionRepository.save(intersection);
		int intersectionId = savedIntersection.getIntersectionId();

		// Act - Turn off all lights
		ResponseEntity<Void> response = restTemplate.exchange(
				"/api/intersections/{intersectionId}/lights/off",
				HttpMethod.PUT,
				null,
				Void.class,
				intersectionId);

		// Assert - Check response status
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Verify lights are off in the database
		Intersection updatedIntersection = intersectionRepository.findById(intersectionId).orElse(null);
		assertNotNull(updatedIntersection);
		assertEquals(LightStatus.off, updatedIntersection.getRoadALightColor());
		assertEquals(LightStatus.off, updatedIntersection.getRoadBLightColor());
	}

	@Test
	public void testChangeRoadALightFromRedToGreenChangesRoadBLightToRed() {
		// Arrange - Create and save an intersection
		Intersection intersection = new Intersection();
		intersection.setRoadALightColor(LightStatus.red);
		intersection.setRoadBLightColor(LightStatus.green);

		Intersection savedIntersection = intersectionRepository.save(intersection);
		int intersectionId = savedIntersection.getIntersectionId();
		RoadDesignation roadDesignation = RoadDesignation.road_A;
		LightStatus lightStatus = LightStatus.green;

		// Act - Set light status for Road A
		ResponseEntity<Void> response = restTemplate.exchange(
				"/api/intersections/{intersectionId}/roads/{roadDesignation}/lights/{lightStatus}",
				HttpMethod.PUT,
				null,
				Void.class,
				intersectionId, roadDesignation, lightStatus);

		// Assert - Check response status
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Verify light status is updated in the database
		Intersection updatedIntersection = intersectionRepository.findById(intersectionId).orElse(null);
		assertNotNull(updatedIntersection);
		assertEquals(LightStatus.green, updatedIntersection.getRoadALightColor());
		assertEquals(LightStatus.red, updatedIntersection.getRoadBLightColor());
	}

	@Test
	public void testSetLightStatusRoadBToYellowChangesRoadAToRed() {
		// Arrange - Create and save an intersection
		Intersection intersection = new Intersection();
		intersection.setRoadALightColor(LightStatus.green);
		intersection.setRoadBLightColor(LightStatus.red);

		Intersection savedIntersection = intersectionRepository.save(intersection);
		int intersectionId = savedIntersection.getIntersectionId();
		RoadDesignation roadDesignation = RoadDesignation.road_B;
		LightStatus lightStatus = LightStatus.yellow;

		// Act - Set light status for Road B
		ResponseEntity<Void> response = restTemplate.exchange(
				"/api/intersections/{intersectionId}/roads/{roadDesignation}/lights/{lightStatus}",
				HttpMethod.PUT,
				null,
				Void.class,
				intersectionId, roadDesignation, lightStatus);

		// Assert - Check response status
		assertEquals(HttpStatus.OK, response.getStatusCode());

		// Verify light status is updated in the database
		Intersection updatedIntersection = intersectionRepository.findById(intersectionId).orElse(null);
		assertNotNull(updatedIntersection);
		assertEquals(LightStatus.yellow, updatedIntersection.getRoadBLightColor());
		assertEquals(LightStatus.red, updatedIntersection.getRoadALightColor());
	}

	@Test
	public void testIntersectionNotFound() {
		// Act - Try to turn off lights for a non-existent intersection
		int nonExistentId = 9999;
		ResponseEntity<Void> response = restTemplate.exchange(
				"/api/intersections/{intersectionId}/lights/off",
				HttpMethod.PUT,
				null,
				Void.class,
				nonExistentId);

		// Assert - We expect the request to succeed even if the intersection is not found
		// (this matches the behavior of the service implementation)
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}