import lejos.nxt.ADSensorPort;
import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Datalogger;

public class Detector extends Thread{
	
	/** instance variables*/
	
	
	UltrasonicSensor _ultrasonic;
	TouchSensor _rightT;
	TouchSensor _leftT;
	NXTRegulatedMotor _motor;
	int _minDistance;
	int _dectAngle;
	boolean alert = false;
	
	Datalogger dl = new Datalogger();
	
	public Detector(NXTRegulatedMotor motor, SensorPort USport, ADSensorPort rightT, ADSensorPort leftT){
		_ultrasonic = new UltrasonicSensor(USport);
		_rightT = new TouchSensor(rightT);
		_leftT = new TouchSensor(leftT);
		_motor = motor;
	}
	
	public void detect(){
		detectTo(90);
		detectTo(-90);
	}
	
	public void detectTo(int limitAngle){
		int oldAngle = _motor.getTachoCount();
	      int distance = 0;
	      while (_motor.isMoving())
	      {
	         short angle = (short) _motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	            distance = _ultrasonic.getDistance();
	            oldAngle = angle;
	         }
	         if (distance > _minDistance) {
					_minDistance = distance;
					_dectAngle = angle;
				}
	         checkTouch();
	         checkDistance();
	      }
	      
	}
	
	public void checkDistance(){
		if(_minDistance < 10  && (_dectAngle < 60 && _dectAngle >-60) ){
			alarm();
		}
	}
	
	public void checkTouch(){
		boolean right = _rightT.isPressed();
		boolean left = _leftT.isPressed();
		
		if(right || left){
			alarm();
		}
	}
	
	public void alarm(){
		alert = true;
		Sound.beep();
	}
	
	public int checkAround(){
		_motor.rotate(0, false);
		detectTo(90);
		int left = _minDistance;
		_motor.rotate(0, false);
		detectTo(-90);
		int right = _minDistance;
		if(left>right){
			return -1;
		}
		else{return 1;}
	}

}