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
	Navigator _nav;
	ScanRecorder _scan;
	int _maxDistance = 0;
	int _PathAngle = 0;
	int distance = 255;
	
	public Detector(NXTRegulatedMotor motor, SensorPort USport, ADSensorPort rightT, ADSensorPort leftT, Navigator nav, ScanRecorder s){
		_ultrasonic = new UltrasonicSensor(USport);
		_rightT = new TouchSensor(rightT);
		_leftT = new TouchSensor(leftT);
		_motor = motor;
		_nav = nav;
		_scan = s;
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
		distance = 255;
		_minDistance = 255;
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
	         checkTouch();
	         checkDistance();
	      }
	      
	}
	
	public void checkDistance(){
		if(_minDistance < 15 ){
			alarm();
			System.out.println("Something too close"); //B

		}
	}
	
	public void checkTouch() {
			boolean right = _rightT.isPressed();
			boolean left = _leftT.isPressed();

			if (right || left) {
				alarm();
				if (right) {
				_nav.buttonalertR = true; 
				}
				else if (left) {
					_nav.buttonalertL = true;
				}
				else if (left && right) {
					_nav.buttonalertR = true;
					_nav.buttonalertL = true;
				}
				System.out.println("Button has been pressed");//B

			}
		}
	
	public void alarm(){
		_nav.obstacle(getDistance(),getAngle());
		Sound.beep();
		System.out.println("Alarm!"); //B
		
	}
	
	public void checkAround(){
		System.out.println("Checking");
		_scan.rotateTo(-60, false);
		_scan.rotateTo(60, true);
		_maxDistance = 0;
		_PathAngle = 0;
		int checkdistance = 0;
		int oldAngle = _motor.getTachoCount();
		      while (_motor.isMoving()) { 
		      {
		         short angle = (short) _motor.getTachoCount();
		         if (angle != oldAngle)
		         {
		            checkdistance = _ultrasonic.getDistance();
		            oldAngle = angle;
		         }
		         if (checkdistance > _maxDistance) {
						_maxDistance = checkdistance;
						_PathAngle = angle;
					}
		      }
		   }
	}
		
//		int left = _minDistance;
//		_scan.rotateTo(60, true);
//		detectTo();
//		int right = _minDistance;
//		if(left>right){
//			return -1;
//		}
//		else{return 1;}
//	}
	
	public void run(){
		while (true) {
		while(_nav.alert==false){
			detect();
		}
		distance = 255;
	}

}}