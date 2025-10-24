package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSubsystem extends SubsystemBase {
    private static final double SHOOT_VOLTAGE = 6.0;

    private final TalonFX motor;

    public ShooterSubsystem() {
        motor = new TalonFX(8);

        // Set the motor to coast mode so that it can spin down slowly
        // when stopping.
        TalonFXConfiguration motorConfig = new TalonFXConfiguration();
        motorConfig.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        // Send the configuration to the motor controller.
        motor.getConfigurator().apply(motorConfig);
    }

    public Command idle() {
        return this.run(() -> {
            // Stop the flywheel to conserve battery power.
            motor.setControl(new NeutralOut());
        });
    }

    public Command spinFlywheel() {
        return this.run(() -> {
            motor.setControl(new VoltageOut(SHOOT_VOLTAGE));
        });
    }
}
