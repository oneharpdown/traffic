package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.LightStatus;
import dev.rygen.intersectionlightcontroller.RoadDesignation;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class IntersectionService {

    private final IntersectionRepository intersectionRepository;

    public IntersectionService(IntersectionRepository intersectionRepository) {
        this.intersectionRepository = intersectionRepository;
    }

    public Intersection createIntersection(Intersection intersection) {
        return this.intersectionRepository.save(intersection);
    }

    public void turnOffAllLightsAtIntersection(int intersectionId) {
        var intersectionOption = this.intersectionRepository.findById(intersectionId);
        intersectionOption.ifPresentOrElse(intersection -> {
            intersection.turnOffLights(); intersectionRepository.save(intersection);
            }, () -> log.warn("Intersection {} not found", intersectionId));
    }

    public void setLightStatus(int intersectionId, RoadDesignation roadDesignation, LightStatus lightStatus) {
        var intersectionOption = this.intersectionRepository.findById(intersectionId);
        intersectionOption.ifPresentOrElse(intersection -> {
            switch (roadDesignation) {
                case road_A: intersection.setRoadALightColor(lightStatus);
                break;
                case road_B: intersection.setRoadBLightColor(lightStatus);
                break;
            }
            intersectionRepository.save(intersection);
        }, () -> log.warn("Intersection {} not found", intersectionId));

    }

}
