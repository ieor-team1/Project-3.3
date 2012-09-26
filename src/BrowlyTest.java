import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;


public class BrowlyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LightSensor lightSensor = new LightSensor(SensorPort.S4);
		Detector d = new Detector(Motor.B, SensorPort.S3, SensorPort.S1, SensorPort.S2);
		ScanRecorder s = new ScanRecorder(Motor.B, lightSensor);
		//System.out.println(d.alert);
		Button.waitForAnyPress();
		while(d.alert==false){
			d.detect();
			//System.out.println("1: " + d.alert);
		}
		Button.waitForAnyPress();
		d.alert = false;

	}

}
