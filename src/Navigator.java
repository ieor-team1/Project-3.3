import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * uses ScanRecorder, Pilot and LightSensor classes to get the data from the
 * sensors and control the robot
 */
public class Navigator {

	float wheelDiameter = 5.6f; // data for DifferentialPilot class
	float trackWidth = 11.4f;
	LightSensor lightSensor = new LightSensor(SensorPort.S4); //
	DifferentialPilot pilot = new DifferentialPilot(wheelDiameter, trackWidth, Motor.A, Motor.C); // Pilot class
	Scanner s = new Scanner(Motor.B, lightSensor);
	Detector d = new Detector(Motor.B, SensorPort.S3, SensorPort.S1, SensorPort.S2, this, s);
	Avoider avoider = new Avoider(pilot, d, this);
	
	/**
	 * Changes scan sweep direction. (left to right or right to left) Pass it to
	 * the ScanRecorder by Scan(i) The aim was to minimize the time between
	 * recordings and changes in the steer
	 */
	int i = 0;
	double gain = 1.2f;
	boolean alert = false;
	boolean buttonalertR = false;
	boolean buttonalertL = false;
	int _angle;
	int _lightAngle;
	int _distance;
	int maxLightValue = 55;

	/** control the responsiveness of the steer function */
	
	public void tryAvoid(){
		avoider.avoid(13, 9);
	}

	public void toLight() {
		//pilot.setTravelSpeed(Battery.getVoltage()*100);
		pilot.setTravelSpeed(30);
		pilot.setAcceleration(300);
		s.setSpeed(500);
		d.start();
		/**
		 * We knew the maximum speed of the robot is limited by it's battery, so
		 * we set the speed at some large number and then the motor will work at
		 * the maximum it can
		 */
		while (true) {
		while (alert == false) {
			int maxLight = s.scan(i);
			System.out.println("Max Light = " + maxLight + " Angle ="+ s.getTargetBearing() + " " + i);
			if (maxLight > maxLightValue) {
				pilot.stop();
				pilot.rotate(-(Math.signum(s.getTargetBearing())*180+s.getTargetBearing()));
				s.rotateTo(-80);
				maxLight = s.scanTo(80);
				pilot.rotate(-s.getTargetBearing());
				pilot.steer(0);
				//pilot.rotate(180);
			}
				
			
			if(alert == false) {	
				pilot.steer(-s.getTargetBearing() * gain);
				//pilot.steer(0);
			}
			else {	
					_lightAngle = s.getTargetBearing();
					while(alert){
					//pilot.stop();
						avoider.avoid(_angle, _distance);
						alert = false;
						while(pilot.isMoving()){
						}
						pilot.rotate(-d._PathAngle);
						pilot.travel(15); //B
					}
					
			}
			i++;
		}
		//pilot.stop();
		}
	
	}
	
	

	public void obstacle(int distance, int angle){
		alert = true;
		pilot.stop(); 
		_angle = angle; //angle of the obstacle
		_distance = distance; //distance of the obstacle
	}
}