import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;


public class Avoider {
	
	DifferentialPilot _pilot;
	Detector _dec;
	Navigator _nav;
	int _overangle = 7;

	public Avoider(DifferentialPilot p1, Detector dec, Navigator nav){
		_pilot = p1;
		_dec = dec;
		_nav = nav;
		//_minavoid = _nav._distance; 
	}
	public void avoid(int angle, int distance){ //give touch sensor is hit
		_pilot.stop();
		_pilot.travel(-5);
		_dec.checkAround();

		System.out.println("Path Angle" + _dec._PathAngle);
		if (_dec._PathAngle >= 0) {
		_pilot.rotate(_dec._PathAngle + _overangle);
		}
		else if (_dec._PathAngle < 0) {
			_pilot.rotate(_dec._PathAngle - _overangle);
			}
		System.out.println("Max distance" + _dec._maxDistance);
		if (_dec._maxDistance < 50 ) {
			_pilot.travel(_dec._maxDistance/1.3, true);
		}
		else if (_dec._maxDistance >= 50) {
			_pilot.travel(30, true);
		}
	}
}
