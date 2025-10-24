package frc.robot.subsystems;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
  private final TalonSRX leftMotor1;
  private final TalonSRX leftMotor2;
  private final TalonSRX rightMotor1;
  private final TalonSRX rightMotor2;

  public DriveSubsystem() {
    // Initialize our motor objects.
    // The number parameters are the CAN IDs assigned to each
    // motor controller using Phoenix Tuner.
    leftMotor1 = new TalonSRX(1);
    leftMotor2 = new TalonSRX(2);
    rightMotor1 = new TalonSRX(3);
    rightMotor2 = new TalonSRX(4);
  }

  public Command arcadeDrive(
      Supplier<Double> forwardSupplier,
      Supplier<Double> turnSupplier) {
    return this.run(() -> {
      // Get the latest control inputs.
      double forward = forwardSupplier.get();
      double turn = turnSupplier.get();

      // Calculate how fast each set of wheels should turn.
      double leftWheels = forward + turn;
      double rightWheels = -forward + turn;

      // Desaturate wheel speeds if needed.
      double maxOutput = Math.max(Math.abs(leftWheels), Math.abs(rightWheels));
      if (maxOutput > 1.0) {
        // Too fast! Our motor controllers aren't capable of this speed, so we
        // need to slow it down.
        leftWheels = leftWheels / maxOutput;
        rightWheels = rightWheels / maxOutput;
      }

      // Tell the motor controllers to spin the motors!
      leftMotor1.set(ControlMode.PercentOutput, leftWheels);
      leftMotor2.set(ControlMode.PercentOutput, leftWheels);
      rightMotor1.set(ControlMode.PercentOutput, rightWheels);
      rightMotor2.set(ControlMode.PercentOutput, rightWheels);
    });
  }
}
