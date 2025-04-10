package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.LightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "intersection")
public class Intersection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intersection_id")
    private int intersectionId;

    @Column(name = "road_a_light_color")
    private LightStatus roadALightColor;

    @Column(name = "road_b_light_color")
    private LightStatus roadBLightColor;

    @Column(name = "powered_on")
    private boolean poweredOn;

    /**
     * Turn off lights for both roads
     */
    public void turnOffLights() {
        this.roadALightColor = LightStatus.off;
        this.roadBLightColor = LightStatus.off;
    }

    /**
     * Sets light status for Road A and automatically adjusts Road B
     * to maintain traffic safety (opposing lights)
     *
     * @param newLightStatus The new light status for Road A
     */
    public void setRoadALightColor(LightStatus newLightStatus) {
        this.roadALightColor = newLightStatus;
        updateOpposingRoadLight(newLightStatus, true);
    }

    /**
     * Sets light status for Road B and automatically adjusts Road A
     * to maintain traffic safety (opposing lights)
     *
     * @param newLightStatus The new light status for Road B
     */
    public void setRoadBLightColor(LightStatus newLightStatus) {
        this.roadBLightColor = newLightStatus;
        updateOpposingRoadLight(newLightStatus, false);
    }

    /**
     * Updates the opposing road's light based on the new status of the current road
     *
     * @param newLightStatus The new light status for the current road
     * @param isRoadA true if Road A is being updated, false if Road B
     */
    private void updateOpposingRoadLight(LightStatus newLightStatus, boolean isRoadA) {
        LightStatus opposingLightStatus = (newLightStatus == LightStatus.red)
                ? LightStatus.green
                : LightStatus.red;

        if (isRoadA) {
            this.roadBLightColor = opposingLightStatus;
        } else {
            this.roadALightColor = opposingLightStatus;
        }
    }
}