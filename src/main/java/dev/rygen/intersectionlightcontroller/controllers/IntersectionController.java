package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.LightStatus;
import dev.rygen.intersectionlightcontroller.RoadDesignation;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/intersections")
public class IntersectionController {

    private final IntersectionService intersectionService;

    public IntersectionController(IntersectionService intersectionService) {
        this.intersectionService = intersectionService;
    }

    @PostMapping
    public ResponseEntity<Intersection> createIntersection(@RequestBody Intersection intersection) {
        log.info("Creating new intersection");
        Intersection createdIntersection = intersectionService.createIntersection(intersection);
        return new ResponseEntity<>(createdIntersection, HttpStatus.CREATED);
    }

    @PutMapping("/{intersectionId}/lights/off")
    public ResponseEntity<Void> turnOffAllLights(@PathVariable int intersectionId) {
        log.info("Turning off all lights at intersection {}", intersectionId);
        intersectionService.turnOffAllLightsAtIntersection(intersectionId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{intersectionId}/roads/{roadDesignation}/lights/{lightStatus}")
    public ResponseEntity<Void> setLightStatus(
            @PathVariable int intersectionId,
            @PathVariable RoadDesignation roadDesignation,
            @PathVariable LightStatus lightStatus) {

        log.info("Setting light status for road {} at intersection {} to {}",
                roadDesignation, intersectionId, lightStatus);

        intersectionService.setLightStatus(intersectionId, roadDesignation, lightStatus);
        return ResponseEntity.ok().build();
    }
}