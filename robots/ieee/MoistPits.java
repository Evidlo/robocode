package ieee;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * MoistPits - a robot by (your name here)
 */
public class MoistPits extends AdvancedRobot
{
	/**
	 * run: MoistPits's default behavior
	 */
	double pEnergy = 100;
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		//setColors(Color.blue,Color.white,Color.white); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
//			ahead(100);
			turnGunRight(360);
//			back(100);
			turnGunRight(360);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		double energydiff = pEnergy - e.getEnergy();
		double bearing = e.getBearing();
		double velocity = e.getVelocity();
		setTurnRight(e.getBearing()+90-30);
		if(energydiff > 0) {
			//setAhead((e.getDistance()/4+25));
		}
		double alpha = Math.asin((velocity*Math.sin(180 - bearing))/17);
		turnGunRight(alpha);
		fire(1);
		turnGunLeft(2);
		fire(1);
		turnGunRight(4);
		fire(1);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
    setTurnGunLeft(e.getBearing());
		fire(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		if(Math.abs(e.getBearing())>90){
		ahead(20);
		}
		else{
			back(20);
		}
	}	

    public void onHitRobot(HitRobotEvent e) {
        //Turn towards enemy robot, fire twice
        setBack(10);
        turnGunLeft(e.getBearing());
        fire(2);
    }	


}
