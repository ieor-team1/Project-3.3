import lejos.nxt.*;
import lejos.util.Datalogger;


/**
 * records the results of a 160 degree scan, maximun light and corresponding angle intensity 
 * 
 * based on ScanRecorder @author owner.GLASSEY
 * modified by Browly and Almudena for Project 3 Milestone 1
 */
public class Scanner {

	public Scanner(NXTRegulatedMotor theMotor, LightSensor eye) {
		motor = theMotor;
		_eye = eye;
		_eye.setFloodlight(false);
	}

	/**
	 * returns the angle at which the maximum light intensity was found
	 * 
	 * @return
	 */
	public int getTargetBearing() {
		return _targetBearing;
	}

	/**
	 * returns the maximum light intensity found during the scan
	 * 
	 * @return light intensity
	 */
	public int getLight() {
		return _maxLight;
	}

	/**
	 * returns the angle in which the light sensor is pointing
	 * 
	 * @return the angle
	 */
	public int getHeadAngle() {

		return motor.getTachoCount();
	}

	/**
	 * sets the motor sped in deg/sec
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		motor.setSpeed(speed);
	}

	/**
	 * scan from current head angle to limit angle and write the angle and light
	 * sensor value to the datalog
	 * 
	 * @param limitAngle
	 */
	public int scanTo(int limitAngle) {
		int oldAngle = motor.getTachoCount();
		motor.rotateTo(limitAngle, true);
		light = 0;
		_maxLight = 0;
		while (motor.isMoving()) {
			short angle = (short) motor.getTachoCount();
			if (angle != oldAngle) {
				light = _eye.getLightValue();
				if (light > _maxLight) {
					_maxLight = light;
					_targetBearing = angle;
				}
				oldAngle = angle;
			}
			// Thread.yield();
		}
		System.out.println(_maxLight);
		return _maxLight;
	}

	/**
	 * rotate the scanner head to the angle
	 * 
	 * @param angle
	 * @param instantReturn
	 *            if true, the method is non-blocking
	 */
	public void rotateTo(int angle, boolean instantReturn) {
		motor.rotateTo(angle, instantReturn);
	}

	/**
	 * rotates the scaner head to angle; returns when rotation is complete
	 * 
	 * @param angle
	 */
	public void rotateTo(int angle) {
		rotateTo(angle, false);
	}

	/**
	 * scan between -80 and 80 degrees
	 * 
	 * @param args
	 */
	public int scan(int i) {
		/**
		 * Modified the scan function so it doesn't write to to datalog but
		 * rather returns the angle where the maximum light value is.
		 */
		//int maxPrinting = scanTo((int) Math.pow(-1, i) * (_targetBearing+20));
		int maxPrinting = scanTo((int) Math.pow(-1, i) * 45);
		_angle1 = getTargetBearing();
		return maxPrinting;
	}

	/******* instance variabled ***************/
	NXTRegulatedMotor motor;
	LightSensor _eye;
	/** Angle of maximum light.*/
	int _targetBearing = 60; 
	int _maxLight;
	int light;
	/** Debugging purposes. Returns _maxlight value*/
	int maxPrinting; 
	boolean _found = false;
	/** Same value as _targetBearing. */
	int _angle1;
}
