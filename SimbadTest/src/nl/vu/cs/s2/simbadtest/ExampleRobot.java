package nl.vu.cs.s2.simbadtest;

import javax.vecmath.Vector3d;
import simbad.sim.Agent;
import simbad.sim.RobotFactory;
import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;

public class ExampleRobot extends Agent {

	private String currentMode;
	RangeSensorBelt sonar;
	RangeSensorBelt bumper;
	CameraSensor camera;

    public ExampleRobot(Vector3d position, String name) {
        super(position, name);
        
        // Add sonars
        sonar = RobotFactory.addSonarBeltSensor(this, 4);
        
        // Add bumpers
        bumper = RobotFactory.addBumperBeltSensor(this, 4);
        
       // Add camera sensor
        camera = RobotFactory.addCameraSensor(this);
    }

    /** This method is called by the simulator engine on reset. */
    public void initBehavior() {
        System.out.println("I exist and my name is " + this.name);
    }
    
    private void move() {
    	// the robot's speed is always 0.5 m/s
        this.setTranslationalVelocity(0.5);
        
		// frequently change orientation
        if ((getCounter() % 100) == 0) {
            setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));
        } 
    }
    
    private void measureDist() {
    	// Get distance from sonars for every 20 frames
    	 if (getCounter()%20==0){
             // print each sonars measurement
             for (int i=0; i< sonar.getNumSensors(); i++) {
                 double distance = sonar.getMeasurement(i); 
                 System.out.println("Sonar " + i + " : Distance : " + distance);
             }
    	 }
    }
    
    private void detectHit() {
    	// Get hit from bumpers for every 20 frames
   	 	if (getCounter()%20==0){
   	 		// print each bumper's hit state 
   	 		for (int i=0; i< bumper.getNumSensors(); i++) {
   	 			boolean hit = bumper.hasHit(i);
   	 			System.out.println("Bumper " + i + " : Has a hit = " +hit);
   	 		}
   	 	}	 
    }

    /** This method is call cyclically (20 times per second) by the simulator engine. */
    public void performBehavior() {
    	
	    	// Distance from Sonar sonars
	    	measureDist();
	    	
	    	if(this.collisionDetected()) {
	    		this.currentMode = "avoidObstacle";
		    	// Get hit state from Bumper sonars
		    	detectHit();
	    	} else {
	    		this.currentMode = "goAround";
	    	}
	        
	    	if(this.currentMode == "goAround") {
	    		// Move the robots
	    		move();
	        } else {
	        	// don't move : stays and rotates until it gets an way out!
	        	this.setTranslationalVelocity(0);
	        	// rotate only until obstacle is not there
	        	setRotationalVelocity(Math.PI / 2);
	        }

    	}
}