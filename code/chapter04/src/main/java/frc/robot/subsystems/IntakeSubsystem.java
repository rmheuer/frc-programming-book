package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
    private static final Angle RETRACTED_ANGLE = Units.Degrees.of(90.0);
    private static final Angle EXTENDED_ANGLE = Units.Degrees.of(0.0);

    private static final double ROLLER_VOLTAGE = 3.0;

    private final TalonFX rollerMotor;
    private final TalonFX pivotMotor;

    public IntakeSubsystem() {
        rollerMotor = new TalonFX(5);
        pivotMotor = new TalonFX(6);
    
        // Set the roller to coast mode, since it doesn't need to hold
        // anything in place.
        TalonFXConfiguration rollerConfig = new TalonFXConfiguration();
        rollerConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;
        
        // Set the pivot to brake mode so that it holds the intake steady,
        // and configure the PID control parameters.
        TalonFXConfiguration pivotConfig = new TalonFXConfiguration();
        pivotConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        pivotConfig.Feedback.SensorToMechanismRatio = 25.0 / 1.0;
        pivotConfig.Slot0.kP = 0.0; // Should be tuned to the physical robot
        pivotConfig.Slot0.kD = 0.0; // Should be tuned to the physical robot
        
        // Send the configurations to the motor controller.
        rollerMotor.getConfigurator().apply(rollerConfig);
        pivotMotor.getConfigurator().apply(pivotConfig);

        // Tell the pivot motor the initial position of the intake. Here
        // we're assuming the intake always starts fully retracted.
        pivotMotor.setPosition(RETRACTED_ANGLE);

        // Give the pivot motor an initial control request to keep the intake
        // in the retracted position.
        pivotMotor.setControl(new PositionVoltage(RETRACTED_ANGLE));
    }

    public Command extend() {
        return this.startEnd(
            () -> {
                // This will be run once when the command starts.

                // Tell the pivot motor to extend the intake.
                pivotMotor.setControl(new PositionVoltage(EXTENDED_ANGLE));

                // Tell the roller motor to spin the rollers.
                rollerMotor.setControl(new VoltageOut(ROLLER_VOLTAGE));
            }, 
            () -> {
                // This will be run once when the command stops.

                // Tell the pivot motor to retract the intake.
                pivotMotor.setControl(new PositionVoltage(EXTENDED_ANGLE));

                // Tell the roller motor to turn off the rollers to conserve
                // battery power.
                rollerMotor.setControl(new NeutralOut());
            });
    }
}
