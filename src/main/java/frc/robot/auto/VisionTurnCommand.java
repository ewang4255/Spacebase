package frc.robot.auto;
import edu.wpi.first.wpilibj.command.Command;
import frc.lib.vision.Point;
import frc.lib.vision.Target;
import frc.robot.Robot;
//MAKE SURE DRIVER IS LEFT OF TARGET
/* Uses input recieved by Target Class from Raspberry Pi to turn wheels towards midpoint of targets
   Raspberry Pi outputs vertices of all objects it can detect
   Target Class files these vertices of each detected object into an array
   Target Class finds the center of each object
   DriveTurnCommand calls upon Target index and finds the two largest objects in the indexes and 
   then uses the center of each object to find the center between the two objects
   DriveTurnCommand sets motors in the direction that will direct robot to midpoint
   DriveTurnCommand has a tolerance range from 300-340; anywhere within that range, 
   robot will think it's in center and motors will stop

*/

public class VisionTurnCommand extends Command{

// pi CAM should be at least 8 inches above ground! hatch is 31.5 inches off ground
//WRITE RANGE - 2560 x 1920 4:3; 17 feet plus; 204 inches+ 

//THRESHOLD:
    //lower or higher than certain pt can adjust wheels
    //300 lower threshold, 340 higher threshold; tolerance

/////////PLUG THE DAMN PI INTO RX AND GROUND. RX. NOT TX. RX. RX
//RRRRRRRRRRRRRRRRRRRXXXXXXXXXXXXXXXXXXXXXX

//CAM dimensions: 640 width, 480 height

    public static final int LOWERTHRESHOLD = 300; 
    public static final int HIGHERTHRESHOLD = 340;
    public static final int OUTERLOWERTHRESHOLD = 100; 
    public static final int OUTERHIGHERTHRESHOLD = 540;
   
    public double TURNSPEED = 0.15;
    //private double MaxVelocity = 0.2;
    //private double PValue = 0.025;
   
    private Point MidPoint;
    private Target[] targets;
    private double half = 320; //midpoint of screen

    public VisionTurnCommand(){
        requires(Robot.driveSys);  
        
    }

    @Override
    protected void initialize(){
      System.out.println("VISION STARTS!!! ");        
    }

    @Override
    protected void execute(){
        targets = Robot.visionInput.getVisionPacket();
        MidPoint = Target.getMidpoint(targets);
        
        System.out.println("MIDPOINT: "+ MidPoint);
        
        if(MidPoint.x > HIGHERTHRESHOLD || MidPoint.x < LOWERTHRESHOLD){ //anything within THIS threshold will will make robot stop because it's centered
            // Robot.driveSys.setMotorsLeft(TURNSPEED*(Math.signum(320-MidPoint.x)));
 
             if(MidPoint.x > OUTERLOWERTHRESHOLD && MidPoint.x < OUTERHIGHERTHRESHOLD) { //anything outside HIGHERTHRESHOLD and LOWERTHRESHOLD will trigger a larger range within OUTERLOWERTHRESHOLD and OUTERHIGHERTHRESHOLD for motors to turn
                 
             //slows down proportionally to distance from midpoint
                 if(MidPoint.x < half) { // if midpoint is too far left
                     Robot.driveSys.setMotorsLeft(TURNSPEED*((half-MidPoint.x)/245)); //320 - midpoint = distance from middle of screen; 245 is a little more than half of outer threshold range [100-540)]
                     Robot.driveSys.setMotorsRight(-TURNSPEED*((half-MidPoint.x)/245)); //the reason it's 245 instead of 220 is to slow the motors down a bit faster; otherwise motors will not have time to slow down within the periodic; man if only i had time to figure out PID loops...
                 }
             
                 //slows down proportionally to distance from midpoint
                 if(MidPoint.x > half) { // if midpoint is too far right
                     Robot.driveSys.setMotorsLeft(-TURNSPEED*((MidPoint.x-half)/245));
                     Robot.driveSys.setMotorsRight(TURNSPEED*((MidPoint.x-half)/245)); 
                 }
             }
             else{
                 Robot.driveSys.setMotorsLeft(TURNSPEED);
                 Robot.driveSys.setMotorsRight(-TURNSPEED);
         
             //Robot.driveSys.setMotorsLeft(TURNSPEED*z);
             //midpoint from middle of screen, smaller distance, smaller the speed
             //Robot.driveSys.setMotorsRight(TURNSPEED*Math.signum(320-MidPoint.x));
             //Robot.driveSys.setMotorsRight(TURNSPEED*z);
             }
         }
         else{
             Robot.driveSys.setMotors(0, 0);
         }
     }

    public boolean isFinished(){
        return(MidPoint.x < HIGHERTHRESHOLD && MidPoint.x > LOWERTHRESHOLD);
    }

    public void end(){
        System.out.println("VISIONTURN is DONE!!");
        Robot.driveSys.setMotors(0, 0);
    }


}
