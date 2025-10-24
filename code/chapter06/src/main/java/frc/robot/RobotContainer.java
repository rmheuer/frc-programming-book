// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IndexerSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class RobotContainer {
  private final CommandXboxController driverController;
  private final CommandXboxController operatorController;

  private final DriveSubsystem driveBase;
  private final IntakeSubsystem intake;
  private final IndexerSubsystem indexer;
  private final ShooterSubsystem shooter;

  public RobotContainer() {
    // The number parameters here are the ports of the controllers in Driver Station.
    driverController = new CommandXboxController(0);
    operatorController = new CommandXboxController(1);

    driveBase = new DriveSubsystem();
    intake = new IntakeSubsystem();
    indexer = new IndexerSubsystem();
    shooter = new ShooterSubsystem();

    configureBindings();
  }

  private void configureBindings() {
    // Configure the default controls for the drive base.
    driveBase.setDefaultCommand(driveBase.arcadeDrive(
        () -> MathUtil.applyDeadband(-driverController.getLeftY(), 0.1),
        () -> MathUtil.applyDeadband(driverController.getRightX(), 0.1)));

    // Put the indexer in idle when nothing else is using it.
    indexer.setDefaultCommand(indexer.idle());

    // Also put the shooter in idle by default.
    shooter.setDefaultCommand(shooter.idle());

    // Bind the intake extension to the A button on the operator controller.
    operatorController.a()
        .whileTrue(intake.extend())
        .whileTrue(indexer.receivePieceFromIntake());

    // Bind the shoot control to the B button on the operator controller.
    operatorController.b()
        .whileTrue(indexer.feedPieceToShooter());

    // Bind the flywheels to the left trigger on the operator controller.
    // Use a Trigger to convert the analog input into a digital (boolean) one.
    new Trigger(() -> (operatorController.getLeftTriggerAxis() > 0.5))
        .whileTrue(shooter.spinFlywheel());
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
