package org.talon540.control;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class BatteryVoltageTrigger extends Trigger {
  /**
   * Set the minimum voltage required before the battery safety reporting process starts
   *
   * @param minVoltage minimum voltage [0v, 12v]
   */
  public BatteryVoltageTrigger(double minVoltage) {
    super(() -> RobotController.getBatteryVoltage() < minVoltage);
  }

  public BatteryVoltageTrigger() {
    this(11);
  }
}
