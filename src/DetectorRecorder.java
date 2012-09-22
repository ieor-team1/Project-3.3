import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Datalogger;

public class DetectorRecorder {
	
	/** instance variables*/
	
	UltrasonicSensor _ultrasonic;
	NXTRegulatedMotor _motor;
	
	Datalogger dl = new Datalogger();
	
	public DetectorRecorder(NXTRegulatedMotor motor, SensorPort port){
		_ultrasonic = new UltrasonicSensor(port);
		_motor = motor;
	}
	
	public static void main(String[] args){
		DetectorRecorder d = new DetectorRecorder(Motor.B, SensorPort.S3);
		Button.waitForAnyPress();
		d.detectTo(90);
		d.detectTo(-90);
		d.dl.transmit();
	}
	
	public void detectTo(int limitAngle){
		int oldAngle = _motor.getTachoCount();
	      _motor.rotateTo(limitAngle, true);
	      int distance = 0;
	      while (_motor.isMoving())
	      {
	         short angle = (short) _motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	            distance = _ultrasonic.getDistance();
	            oldAngle = angle;
	            dl.writeLog(angle);
	            dl.writeLog(distance);
	         }
	      }
	}
}