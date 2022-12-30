package org.talon540.sensors.vision;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.jetbrains.annotations.NotNull;
import org.talon540.sensors.vision.VisionFlags.CAMMode;
import org.talon540.sensors.vision.VisionFlags.LEDStates;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

public abstract class VisionSystem implements Sendable {
    protected final VisionCameraMountConfig mountConfig;
    protected final Collection<Consumer<VisionState>> viewEvents = new LinkedHashSet<>();

    protected VisionSystem(@NotNull VisionCameraMountConfig mountConfig) {
        this.mountConfig = mountConfig;
    }

    /**
     * Get the current index of the running vision system pipeline
     *
     * @return current index
     */
    public abstract int getPipelineIndex();

    /**
     * Set the index of the vision system's pipeline
     */
    public abstract void setPipelineIndex(int index);

    /**
     * @return current mode of the LEDs
     */
    public abstract LEDStates getLEDMode();

    /**
     * Set the mode of the LEDs of the vision system
     *
     * @param state target state of the camera
     */
    public abstract void setLEDMode(LEDStates state);

    /**
     * Enables vision system LEDs
     */
    public void enableLEDS() {
        setLEDMode(LEDStates.ON);
    }

    /**
     * Disable vision system LEDs
     */
    public void disableLEDS() {
        setLEDMode(LEDStates.OFF);
    }

    /**
     * Blinks vision system LEDs
     */
    public void blinkLEDS() {
        setLEDMode(LEDStates.BLINK);
    }

    /**
     * Get the current camera mode of the vision system
     *
     * @return current camera mode
     */
    public abstract CAMMode getCamMode();

    /**
     * Set the camera mode of the vision system
     */
    public abstract void setCamMode(CAMMode targetMode);

    /**
     * Whether the vision system target is currently viewed
     *
     * @return view status of target
     */
    public abstract boolean targetViewed();

    /**
     * Get the current vision state data. Returns {@code null} if there are no targets
     */
    public abstract VisionState getVisionState();

    /**
     * Create a trigger that activates whenever a target is viewed. Useful if you need to schedule a command whenever
     * target data is needed.
     *
     * @return target view event trigger
     */
    public Trigger getTargetViewedEvent() {
        return new Trigger(this::targetViewed);
    }

    /**
     * Provide the vision state to the provided event consumer when a target is viewed.\
     *
     * @param event Event consumer that receives the state.
     */
    public void whenViewed(Consumer<VisionState> event) {
        viewEvents.add(event);
    }

    /**
     * Poll the vision camera and feeds event consumers provided with {@link VisionSystem#whenViewed(Consumer)}. Put
     * this in the robot periodic method or addPeriodic in TimedRobot.
     */
    public void poll() {
        if (!targetViewed())
            return;

        viewEvents.forEach(eventConsumer -> eventConsumer.accept(getVisionState()));
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addBooleanProperty(
                "viewed",
                this::targetViewed,
                null
        );
        builder.addDoubleProperty(
                "yaw",
                () -> targetViewed() ? getVisionState().getYaw() : 0,
                null
        );
        builder.addDoubleProperty(
                "pitch",
                () -> targetViewed() ? getVisionState().getPitch() : 0,
                null
        );
        builder.addDoubleProperty(
                "skew",
                () -> targetViewed() ? getVisionState().getSkew() : 0,
                null
        );
        builder.addDoubleProperty(
                "area",
                () -> targetViewed() ? getVisionState().getArea() : 0,
                null
        );
        builder.addDoubleProperty(
                "latency",
                () -> targetViewed() ? getVisionState().getPipelineLatency() : 0,
                null
        );
        builder.addDoubleProperty(
                "timestamp",
                () -> targetViewed() ? getVisionState().getStateTimestamp() : 0,
                null
        );
        builder.addDoubleProperty(
                "pipeline",
                this::getPipelineIndex,
                null
        );
        builder.addStringProperty(
                "LEDMode",
                () -> getLEDMode().toString(),
                null
        );
    }
}
