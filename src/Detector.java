import lejos.nxt.ADSensorPort;
import lejos.nxt.Button;
import lejos.nxt.Motor;
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
	int _minDistance = 255;
	int _dectAngle;
	boolean alert = false;
    int distance = 255;
	Datalogger dl = new Datalogger();
	
	public Detector(NXTRegulatedMotor motor, SensorPort USport, ADSensorPort rightT, ADSensorPort leftT){
		_ultrasonic = new UltrasonicSensor(USport);
		_rightT = new TouchSensor(rightT);
		_leftT = new TouchSensor(leftT);
		_motor = motor;
	}
	
	public void detect(){
		if (alert == false){
		detectTo(90);
		detectTo(-90);
		}
	}
	
	public void detectTo(int limitAngle){
		int oldAngle = _motor.getTachoCount();
		_minDistance = 255;
	      System.out.println("Motor: " +_motor.isMoving()); //B
	      while (_motor.isMoving())
	      {
	         short angle = (short) _motor.getTachoCount();
	         if (angle != oldAngle)
	         {
	            distance = _ultrasonic.getDistance();
	            oldAngle = angle;
	         }
	         if (distance < _minDistance) {
					_minDistance = distance;
					_dectAngle = angle;
				}
	         System.out.println("angle: " + _dectAngle); //B
	         System.out.println("distance: " + _minDistance);//B
	         System.out.println(alert); //B
	         checkTouch();
	         checkDistance();
	      }
	      
	}
	
	public void checkDistance(){
		if(_minDistance < 20  && (_dectAngle < 60 && _dectAngle >-60) ){
			alarm();
			System.out.println("Something too close"); //B
			Button.waitForAnyPress();
		}
	}
	
	public void checkTouch() {
			boolean right = _rightT.isPressed();
			boolean left = _leftT.isPressed();

			if (right || left) {
				alarm();
				System.out.println("Button has been pressed");//B
				Button.waitForAnyPress();
			}
		}
	
	public void alarm(){
		alert = true;
		Sound.beep();
		System.out.println("Hello"); //B
		
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