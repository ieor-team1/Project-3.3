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
	ScanRecorder s = new ScanRecorder(Motor.B, lightSensor);
	Detector d = new Detector(Motor.B, SensorPort.S3, SensorPort.S1, SensorPort.S2, this);
	Avoider avoider = new Avoider(pilot);
	
	/**
	 * Changes scan sweep direction. (left to right or right to left) Pass it to
	 * the ScanRecorder by Scan(i) The aim was to minimize the time between
	 * recordings and changes in the steer
	 */
	int i = 0;
	double gain = 1.2f;
	boolean alert = false;
	int _angle;
	int _distance;

	/** control the responsiveness of the steer function */

	public void toLight() {
		pilot.setTravelSpeed(30);
		d.start();
		/**
		 * We knew the maximum speed of the robot is limited by it's battery, so
		 * we set the speed at some large number and then the motor will work at
		 * the maximum it can
		 */
		while (true) {
		while (alert == false) {
			s.setSpeed(700);
			int maxLight = s.scan(i);
			System.out.println("Max Light = " + maxLight + " Angle ="+ s.getTargetBearing() + " " + i);
			if (maxLight > 55) {
				pilot.stop();
				pilot.rotate(180);
			}
				//pilot.steer(0);
			
			if(alert == false)	pilot.steer(-s.getTargetBearing() * gain);
			else {	pilot.stop();
					avoider.avoid(_angle, _distance);
					Button.waitForAnyPress();
					alert = false;
			}
			i++;
		}
		//pilot.stop();
		}
	
	}
	
	

	public void obstacle(int angle, int distance){
		alert = true;
		pilot.stop();
		_angle = angle;
		_distance = distance;
	}
}