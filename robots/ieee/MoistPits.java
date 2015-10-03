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
	double bheight;
	double bwidth;
	double pEnergy = 100;
	double maxDistance;
	int direction = 1;
	public void run() {
		bheight = getBattleFieldHeight();
		bwidth = getBattleFieldWidth();

		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		//setColors(Color.blue,Color.white,Color.white); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			quickMove();
			////////////
			updateDirection();

			////////SWITCH THIS WHATEVER SCANNING FUNCTION
			turnGunRight(180);
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energydiff = pEnergy - e.getEnergy();
		double bearing = e.getBearing();
		double velocity = e.getVelocity();
		double alpha;
		//setTurnRight(e.getBearing()+90-30);
		if(energydiff > 0 && energydiff <= 3) {
			direction = (direction == 1) ? direction - 1 : direction + 1;
			quickMove();
		}
		pEnergy = e.getEnergy();


		//REPLACE THIS WITH AIMING CODE
		if(velocity > 0)
			alpha = Math.asin((velocity*Math.sin(180 - bearing))/17);
		else 
			alpha = 0;
		turnGunLeft(alpha);
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
	public void maxTravel(int direction) {
		if(direction == 1) {
			if(getHeading() == 0)  { //North
				maxDistance = getY();
			}
			else if (getHeading() == 90) {//East
				maxDistance = getX();
			}
			else if(getHeading() == 180)  { //North
				maxDistance = bheight - getY();
			}
			else if (getHeading() == 270) {//East
				maxDistance = bwidth - getX();
			}
			else {
				maxDistance = 50;
			}			
		}
		else {
			if(getHeading() == 0)  { //North
				maxDistance = bheight - getY();
			}
			else if (getHeading() == 90) {//East
				maxDistance = bwidth - getX();
			}
			else if(getHeading() == 180)  { //North
				maxDistance = getY();
			}
			else if (getHeading() == 270) {//East
				maxDistance = getX();
			}
			else {
				maxDistance = 50;
			}
		}

	}
	public void quickMove() {
			maxTravel(direction);
			//ahead(Math.floor(Math.random()*maxDistance));
			if(direction == 0) {
				setAhead((maxDistance/2) + Math.floor(Math.random()*(maxDistance/2)) - 50);
			}
			else {
				setBack((maxDistance/2) + Math.floor(Math.random()*(maxDistance/2)) - 50);
			}
				
	}
	public void updateDirection() {
			double location = Math.atan((getY()-(bheight/2))/(getX()-(bwidth/2)));
			double angle = Math.atan(bheight/bwidth);
			double offset = 10 + (-20 * direction);
			angle = Math.toDegrees(angle);
			location = Math.toDegrees(location);
			if(getX() < bwidth/2) {
				location += 180;
			}
			location = location % 360;
			//location += (180 * direction);
			if(location > (angle + offset) && location < (180-angle + offset)) {
				setTurnRight((90-getHeading())%360);
			}
			else if (location > (180-angle + offset) && location < (180+angle + offset)) {
				setTurnRight((0-getHeading())%360);
			}		
			else if (location > (180 + angle + offset) || (location > -90 && location < 0 - angle + offset)) {
				setTurnRight((270-getHeading())%360);
			}
			else {
				setTurnRight((180-getHeading())%360);
			}
			System.out.println("Location: " + location + ", Angle: " + angle + ", Heading: " + getHeading());
	}
}
