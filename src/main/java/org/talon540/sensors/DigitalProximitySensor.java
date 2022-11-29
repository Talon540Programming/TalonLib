package org.talon540.sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.button.Button;

public class DigitalProximitySensor extends Button {
    private final DigitalInput sensor;
    private final ProximitySensorTypes type;

    public DigitalProximitySensor(int DIOPort, ProximitySensorTypes type) {
        this.sensor = new DigitalInput(DIOPort);
        this.type = type;
    }

    public ProximitySensorTypes getType() {
        return type;
    }

    @Override
    public boolean get() {
        switch (this.type) {
            case HALLSensor:
            case LimitSwitch:
            case BreamBreak:
            default:
                return sensor.get();
        }
    }
}
