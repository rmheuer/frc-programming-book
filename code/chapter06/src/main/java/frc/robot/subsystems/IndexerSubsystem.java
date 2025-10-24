package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerSubsystem extends SubsystemBase {
  private static final double RECEIVE_VOLTAGE = 3.0;
  private static final double FEED_VOLTAGE = 5.0;

  private final TalonFX motor;
  private final DigitalInput beamBreakInput;

  public IndexerSubsystem() {
    motor = new TalonFX(7);

    // The number parameter here is the DIO port on the RoboRIO
    // that the beam break sensor is wired to.
    beamBreakInput = new DigitalInput(0);

    // Set the motor to brake mode so that it holds the game piece in
    // position when not spinning.
    TalonFXConfiguration motorConfig = new TalonFXConfiguration();
    motorConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

    // Send the configuration to the motor controller.
    motor.getConfigurator().apply(motorConfig);
  }

  public boolean hasGamePiece() {
    // Get whether the beam break detects a piece. The beam brake
    // input is backwards, so we need to invert it.
    return !beamBreakInput.get();
  }

  public Command idle() {
    return this.run(() -> {
      // Keep the roller still to conserve battery power.
      motor.setControl(new NeutralOut());
    });
  }

  public Command receivePieceFromIntake() {
    return this.run(() -> {
      if (hasGamePiece()) {
        // If we have a game piece already, hold it in place.
        motor.setControl(new NeutralOut());
      } else {
        // If we don't have a game piece, run the roller to take
        // it from the intake.
        motor.setControl(new VoltageOut(RECEIVE_VOLTAGE));
      }
    });
  }

  public Command feedPieceToShooter() {
    return this.run(() -> {
        // Spin the roller to feed the game piece into the shooter.
        motor.setControl(new VoltageOut(FEED_VOLTAGE));
    });
  }
}
