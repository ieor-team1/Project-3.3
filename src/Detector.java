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
    int distance = 255;
	Navigator _nav;
	
	public Detector(NXTRegulatedMotor motor, SensorPort USport, ADSensorPort rightT, ADSensorPort leftT, Navigator nav){
		_ultrasonic = new UltrasonicSensor(USport);
		_rightT = new TouchSensor(rightT);
		_leftT = new TouchSensor(leftT);
		_motor = motor;
		_nav = nav;
	}
	
	public void detect(){
		detectTo();
	}
	
	public int getDistance(){
		return _minDistance;
	}
	
	public int getAngle(){
		return _dectAngle;
	}
	
	
	public void detectTo(){
		int oldAngle = _motor.getTachoCount();
		_minDistance = 255;
	      System.out.println("Motor: " +_motor.isMoving()); //B
	      while (_nav.alert == false)
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
	         System.out.println(_nav.alert); //B
	         checkTouch();
	         checkDistance();
	      }
	      
	}
	
	public void checkDistance(){
		if(_minDistance < 20 ){
			alarm();
			System.out.println("Something too close"); //B

		}
	}
	
	public void checkTouch() {
			boolean right = _rightT.isPressed();
			boolean left = _leftT.isPressed();

			if (right || left) {
				alarm();
				System.out.println("Button has been pressed");//B

			}
		}
	
	public void alarm(){
		_nav.obstacle(getDistance(),getAngle());
		Sound.beep();
		System.out.println("Alarm!"); //B
		
	}
	
	public int checkAround(){
		_motor.rotate(0, false);
		detectTo();
		int left = _minDistance;
		_motor.rotate(0, false);
		detectTo();
		int right = _minDistance;
		if(left>right){
			return -1;
		}
		else{return 1;}
	}
	
	public void run(){
		while (true) {
		while(_nav.alert==false){
			detect();
		}
		distance = 255;
	}

}}