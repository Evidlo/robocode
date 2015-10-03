package ieee;
import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;
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
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		//setColors(Color.blue,Color.white,Color.white); // body,gun,radar
    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        while (true) {
            turnRadarRight(360);
        }
    }   
	/**
	 * onScannedRobot: What to do when you see another robot
	 */

	public void onScannedRobot(ScannedRobotEvent e) {
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
        double a = (360/(2*Math.PI)) * Math.asin((Math.abs(e.getVelocity()) * Math.sin(theta * (2*Math.PI)/360))/17);
        // System.out.println("||E:" + enemy_heading + "||R:" + getGunHeading() + "||Theta:" + theta);
        System.out.println("||vel:" + e.getVelocity() + "||Th" + theta + "||A:" + a);
        setTurnGunRight(normalRelativeAngleDegrees(getHeading() + e.getBearing() - getGunHeading()) + a);
        fire(1);
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
