package org.talon540.hardware;

import org.jetbrains.annotations.NotNull;

public class CANDeviceConfig {
    public final String controller;
    public final int id;

    /**
     * Create a CAN device with a specified controller
     *
     * @param id device ID
     * @param controller name of the CAN controller
     */
    public CANDeviceConfig(int id, @NotNull String controller) {
        this.id = id;
        this.controller = controller;
    }

    /**
     * Construct a CAN Device and use the RIO as the CAN controller
     *
     * @param id device ID
     */
    public CANDeviceConfig(int id) {
        this(
                id,
                ""
        );
    }
}
