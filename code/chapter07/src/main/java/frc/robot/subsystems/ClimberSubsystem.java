package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    // Positions are in rotor rotations of the motors.
    private static final double RETRACT_POSITION = 0.1;
    private static final double EXTEND_POSITION = 50.0;
    private static final double PULL_POSITION = 20.0;

    private final TalonFX leftArmMotor;
    private final TalonFX rightArmMotor;

    public ClimberSubsystem() {
        leftArmMotor = new TalonFX(9);
        rightArmMotor = new TalonFX(10);
    
        // Set the motors to brake mode so that they hold the arms in place.
        TalonFXConfiguration armConfig = new TalonFXConfiguration();
        armConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // We'll just be working with rotor rotations directly.
        armConfig.Feedback.SensorToMechanismRatio = 1.0;

        // We will use PID slot 0 when the robot is on the ground.
        armConfig.Slot0.kP = 0.0; // Should be tuned to the physical robot
        armConfig.Slot0.kD = 0.0; // Should be tuned to the physical robot
        armConfig.Slot0.kG = 0.0; // Should be tuned to the physical robot
        armConfig.Slot0.GravityType = GravityTypeValue.Elevator_Static;

        // We will use PID slot 1 when the robot is hanging.
        armConfig.Slot1.kP = 0.0; // Should be tuned to the physical robot
        armConfig.Slot1.kD = 0.0; // Should be tuned to the physical robot
        armConfig.Slot1.kG = 0.0; // Should be tuned to the physical robot
        armConfig.Slot1.GravityType = GravityTypeValue.Elevator_Static;

        // Send the configuration to the motor controllers. We will use the
        // same configuration for both motors since the arms are identical.
        leftArmMotor.getConfigurator().apply(armConfig);
        rightArmMotor.getConfigurator().apply(armConfig);

        // Tell the motors their initial position. Here we assume that the
        // arms start in their lowest, fully retracted position.
        leftArmMotor.setPosition(0.0);
        rightArmMotor.setPosition(0.0);
    }

    public Command retract() {
        return this.run(() -> {
            // Tell both motors to go to the retracted position.
            // Using PID slot 0 since the robot is not actively climbing.
            leftArmMotor.setControl(new PositionVoltage(RETRACT_POSITION).withSlot(0));
            rightArmMotor.setControl(new PositionVoltage(RETRACT_POSITION).withSlot(0));
        });
    }

    public Command extend() {
        return this.run(() -> {
            // Tell both motors to go to the extended position.
            // Using PID slot 0 since the arms are not actively lifting the robot.
            leftArmMotor.setControl(new PositionVoltage(EXTEND_POSITION).withSlot(0));
            rightArmMotor.setControl(new PositionVoltage(EXTEND_POSITION).withSlot(0));
        });
    }

    public Command pull() {
        return this.run(() -> {
            // Tell both motors to go to the pull position.
            // Using PID slot 1 since the robot is actively lifting itself up.
            leftArmMotor.setControl(new PositionVoltage(PULL_POSITION).withSlot(1));
            rightArmMotor.setControl(new PositionVoltage(PULL_POSITION).withSlot(1));
        });
    }
}
