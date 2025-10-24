// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveSubsystem;

public class RobotContainer {
  private final CommandXboxController driverController;
  private final DriveSubsystem driveBase;

  public RobotContainer() {
    // The number parameter here is the port of the controller in Driver Station.
    driverController = new CommandXboxController(0);
    driveBase = new DriveSubsystem();

    configureBindings();
  }

  private void configureBindings() {
    // Configure the default controls for the drive base.
    driveBase.setDefaultCommand(driveBase.arcadeDrive(
        () -> MathUtil.applyDeadband(-driverController.getLeftY(), 0.1),
        () -> MathUtil.applyDeadband(driverController.getRightX(), 0.1)));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
