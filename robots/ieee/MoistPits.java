package ieee;
import robocode.*;
//import java.awt.Color;
import robocode.util.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
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
	int dir = 1;
	public void run() {
		bheight = getBattleFieldHeight();
		bwidth = getBattleFieldWidth();
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarRight(Double.POSITIVE_INFINITY);
		turnLeft(getHeading() % 90);
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		//setColors(Color.blue,Color.white,Color.white); // body,gun,radar

		// Robot main loop
		while(true) {
			// Replace the next 4 lines with any behavior you would like
			//quickMove();
			////////////
			//updateDirection();
if (Utils.isNear(getHeadingRadians(), 0D) || Utils.isNear(getHeadingRadians(), Math.PI)) {
					ahead((Math.max(getBattleFieldHeight() - getY(), getY()) - 50) * dir);
				} else {
					ahead((Math.max(getBattleFieldWidth() - getX(), getX()) - 50) * dir);
				}
			turnRight(90 * dir);
			////////SWITCH THIS WHATEVER SCANNING FUNCTION
		}
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		double energydiff = pEnergy - e.getEnergy();
		//setTurnRight(e.getBearing()+90-30);
		if(energydiff > 0 && energydiff <= 3) {
			dir = -dir;
			//quickMove();
		}
		pEnergy = e.getEnergy();

		//SHOOTING
		setTurnRadarRight(normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading()));

        //calculate enemy heading (using their + or - velocity)
		double enemy_heading;
		if(e.getVelocity() >= 0){
			enemy_heading = e.getHeading();
		}
		else{
			enemy_heading = (180 + e.getHeading()) % 360;
		}
		double theta = (180 + getGunHeading() - enemy_heading) % 360;
		if(theta > 180){
			theta = theta - 360;
		}
		double a = (360/(2*Math.PI)) * Math.asin((Math.abs(e.getVelocity()) * Math.sin(theta * (2*Math.PI)/360))/18);
        // System.out.println("||E:" + enemy_heading + "||R:" + getGunHeading() + "||Theta:" + theta);
		System.out.println("||vel:" + e.getVelocity() + "||Th" + theta + "||A:" + a);
		setTurnGunRight(normalRelativeAngleDegrees(getHeading() + e.getBearing() - getGunHeading()) + a);
		if(e.getDistance() < 300 && getGunHeat() == 0)
			setFire(3);

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
