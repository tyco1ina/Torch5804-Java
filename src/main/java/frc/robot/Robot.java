/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//Hardware imports:
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.playingwithfusion.TimeOfFlight;
import com.playingwithfusion.TimeOfFlight.RangingMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

//Other imports:
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

import frc.robot.JoystickButtons;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  //DECLARING GLOBAL VARIABLES
  //Drivebase Motor-related (corresponds to "OPENing" the TalonFX motors in LABView):
  TalonFX driveTalonLeft = new TalonFX(1);
  TalonFX driveTalonLeftFollow = new TalonFX(2);
  TalonFX driveTalonRight = new TalonFX(3);
  TalonFX driveTalonRightFollow = new TalonFX(4);

  //RobotDrive robotDrive = new RobotDrive(driveTalonLeft, driveTalonRight);

  //SpeedControllerGroup leftMotors = new SpeedControllerGroup(leftMotors, rightMotors);

  //Other Motors:
  TalonFX shooterTalon = new TalonFX(5);
  TalonFX shooterTalonFollow = new TalonFX(6);

  TalonFX acceleratorTalon = new TalonFX(7);
  TalonFX conveyorTalon = new TalonFX(8);
  
  TalonFX innerIntakeTalon = new TalonFX(9);
  TalonFX outerIntakeTalon = new TalonFX(10);

  TalonFX climberTalon1 = new TalonFX(1);
  TalonFX climberTalon2 = new TalonFX(2);

  //Joystick:
  Joystick leftStick = new Joystick(0);
  Joystick rightStick = new Joystick(1);

  //Solenoid:
  Solenoid intakePistonSolenoid = new Solenoid(0);
  Solenoid climberPistonSolenoid = new Solenoid(0);

  //Compressor:
  Compressor compressor = new Compressor(0);

  //Time of flight:
  TimeOfFlight timeOfFlight1 = new TimeOfFlight(1);
  TimeOfFlight timeOfFlight2 = new TimeOfFlight(2);
  TimeOfFlight timeOfFlight3 = new TimeOfFlight(3);
  TimeOfFlight timeOfFlight4 = new TimeOfFlight(4);

  //Whatever DIO is:
  //Gyro:
  AnalogGyro gyro = new AnalogGyro(0);

  //Whisker:

  //Creating an instance of the JoystickButtons class so we can use the method in the class:
  JoystickButtons joystickButtons = new JoystickButtons();

  //Variables that were originally in Global Vars.vi
  boolean fireStatus = false;
  boolean aimStatus = false;
  boolean fire = false;
  boolean intakeReverse = false;
  boolean shooterSideForward = false;
  boolean sensor1Override = false;
  boolean autoDrive = false;
  boolean autoDriveStatus = false;
  boolean limelightLEDStatus = false;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    
  }

  @Override
  public void teleopPeriodic() {

    // **************************
    // TELEOP SETUP
    // **************************
    joystickButtons.initializeJoystickButtons();
    //DRIVEBASE TALON STUFF:
    //Talon 2 follows talon 1, talon 4 follows talon 3
    driveTalonLeftFollow.follow(driveTalonLeft);
    driveTalonRightFollow.follow(driveTalonRight);

    //These two lines to help set PercentOutput:
    double leftStickValue = leftStick.getRawAxis(0);
    double rightStickValue = rightStick.getRawAxis(0);

    //Corresponds to "MC SET B/C MODE" (can't figure out what NeutralMode options there are)
    driveTalonLeft.setNeutralMode(NeutralMode.Brake);
    driveTalonRight.setNeutralMode(NeutralMode.Brake);

    //Set percent output for talon1 and talon3 (talon 2 and 4 follow 1 and 3, respectively)
    driveTalonLeft.set(ControlMode.PercentOutput, leftStickValue);
    driveTalonRight.set(ControlMode.PercentOutput, rightStickValue);
  
    //Corresponds to "MC DEAD BAND" in LABView (guess)
    driveTalonLeft.configNeutralDeadband(.1);
    driveTalonRight.configNeutralDeadband(.1);

    //Corresponds to "MC SET SENSOR POS" in LABView (guess)
    driveTalonLeft.setSelectedSensorPosition(0);
    driveTalonRight.setSelectedSensorPosition(0);

    //Missing code for "MC CONFIG PIDF"


    //SHOOTER TALON STUFF:
    shooterTalonFollow.follow(shooterTalon);
    shooterTalonFollow.setInverted(true);
    //Missing code for "MC CONFIG PIDF"

    //ACCELERATOR TALON STUFF:
    acceleratorTalon.enableVoltageCompensation(true);
    acceleratorTalon.configVoltageCompSaturation(12);

    //CONVEYOR TALON STUFF:
    conveyorTalon.enableVoltageCompensation(true);
    conveyorTalon.configVoltageCompSaturation(12);

    //INNER INTAKE STUFF:
    innerIntakeTalon.enableVoltageCompensation(true);
    innerIntakeTalon.configVoltageCompSaturation(12);

    //OUTER INTAKE STUFF:
    outerIntakeTalon.enableVoltageCompensation(true);
    outerIntakeTalon.configVoltageCompSaturation(12);

    //CLIMBER STUFF:
    climberTalon2.follow(climberTalon1);
    climberTalon1.setInverted(true);

    climberTalon1.enableVoltageCompensation(true);
    climberTalon1.configVoltageCompSaturation(12);

    //SOLENOID STUFF:
    //intakePistonSolenoid.setSolenoidChannel?

    //COMPRESSOR STUFF:
    compressor.start();

    //TIME OF FLIGHT:
    timeOfFlight1.setRangingMode(RangingMode.Short, 0);
    timeOfFlight2.setRangingMode(RangingMode.Short, 0);
    timeOfFlight3.setRangingMode(RangingMode.Short, 0);
    timeOfFlight4.setRangingMode(RangingMode.Short, 0);

    //SETTING UP BUTTONS:

    boolean leftTrigger = leftStick.getRawButton(0);
    boolean rightTrigger = rightStick.getRawButton(0);

    boolean leftThumbMain = leftStick.getRawButton(1);
    boolean rightThumbMain = rightStick.getRawButton(1);

    boolean leftThumbLeft = leftStick.getRawButton(2);
    boolean rightThumbLeft = rightStick.getRawButton(2);

    boolean leftThumbRight = leftStick.getRawButton(3);
    boolean rightThumbRight = rightStick.getRawButton(3);

    boolean leftRightArrayTR = leftStick.getRawButton(4);
    boolean rightRightArrayTR = rightStick.getRawButton(4);

    boolean leftRightArrayTM = leftStick.getRawButton(5);
    boolean rightRightArrayTM = rightStick.getRawButton(5);

    boolean leftRightArrayTL = leftStick.getRawButton(6);
    boolean rightRightArrayTL = rightStick.getRawButton(6);

    boolean leftRightArrayBL = leftStick.getRawButton(7);
    boolean rightRightArrayBL = rightStick.getRawButton(7);

    boolean leftRightArrayBM = leftStick.getRawButton(8);
    boolean rightRightArrayBM = rightStick.getRawButton(8);

    boolean leftRightArrayBR = leftStick.getRawButton(9);
    boolean rightRightArrayBR = rightStick.getRawButton(9);

    boolean leftLeftArrayTL = leftStick.getRawButton(10);
    boolean rightLeftArrayTL = rightStick.getRawButton(10);

    boolean leftLeftArrayTM = leftStick.getRawButton(11);
    boolean rightLeftArrayTM = rightStick.getRawButton(11);

    boolean leftLeftArrayTR = leftStick.getRawButton(12);
    boolean rightLeftArrayTR = rightStick.getRawButton(12);

    boolean leftLeftArrayBR = leftStick.getRawButton(13);
    boolean rightLeftArrayBR = rightStick.getRawButton(13);

    boolean leftLeftArrayBM = leftStick.getRawButton(14);
    boolean rightLeftArrayBM = rightStick.getRawButton(14);

    boolean leftLeftArrayBL = leftStick.getRawButton(15);
    boolean rightLeftArrayBL = rightStick.getRawButton(15);

    //Axes Left:
    double leftX = 0;
    double leftY = 0;
    double leftZ = 0;
    double leftSlider = 0;
    //Axis Right:
    double rightX = 0;
    double rightY = 0;
    double rightZ = 0;
    double rightSlider = 0;


    // **************************
    // ACTUAL TELEOP
    // **************************

    if (rightThumbLeft) {
      shooterSideForward = false;
    } else {
      if (rightThumbRight) {
        shooterSideForward = true;
      } else {
        //nothing
      }
    }
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
