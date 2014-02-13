/** machine shop simulation */

package applications;

import java.util.TreeMap;

import utilities.MyInputStream;
import exceptions.MyInputException;
import applications.Machine;

public class MachineShopSimulator {
    
    
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";

    private static int timeNow; 
    private static int numMachines; 
    private static int numJobs; 
    private static int timeLimit; // Maximum allowed time on a machine.
    private static TreeMap<Integer, Machine> machines;
     
    private static Machine nextFreeMachine(){
    	Machine currentShortest = getTheMachine(1);
    	for (Machine theMachine: machines.values()){
            if (theMachine.finishTime < currentShortest.finishTime){
            	currentShortest = theMachine;
            }
    	}
		return currentShortest;
    }
    

    /** input machine shop data */
    private static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        numMachines = (keyboard.readInteger());
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1){
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);
        }
        createMachines(keyboard);
        createJobs(keyboard);
    }

	private static void createMachines(MyInputStream keyboard) {
		machines = new TreeMap<Integer, Machine>();
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= numMachines; j++) {
            int changeTime = keyboard.readInteger();
            if (changeTime < 0){
                throw new MyInputException(CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0);
            }
            machines.put(j,new Machine(j,timeLimit, changeTime));
            
        }
	}

	private static void createJobs(MyInputStream keyboard) {
		Job theJob;
		for (int i = 1; i <= numJobs; i++) {
            System.out.println("Enter number of tasks for job " + i);
            int numTasks = keyboard.readInteger();
            if (numTasks < 1){
                throw new MyInputException(EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK);
            }
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)" + " in process order");
            for (int j = 1; j <= numTasks; j++) {// get tasks for job i
                int theMachineIndex = keyboard.readInteger();
                int theTaskTime = keyboard.readInteger();
                if (theMachineIndex < 1 || theMachineIndex > numMachines || theTaskTime < 1){
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                }
                Machine theMachine = getTheMachine(theMachineIndex);
               
                if (j == 1){// Send job to its first machine.
                	theMachine.jobQ.put(theJob); 
                }
                theJob.addTask(theMachine, theTaskTime);
            } 
            
        }
	}

    /** load first jobs onto each machine */
    private static void startShop() {
    	timeNow = 0;
        for (Machine theMachine: machines.values())
            theMachine.changeState(timeNow, timeLimit);
    }

    /** process all jobs to completion */
    private static void simulate() {
        while (numJobs > 0) {
            Machine nextToFinish = nextFreeMachine();
            timeNow = nextToFinish.timeUntilFinished();
            //Get the next job for nextToFinish
            Job theJob = nextToFinish.changeState(timeNow, timeLimit);
            
            // check if there was a job and if that job has no more tasks
            //and moving job to its next machine
            //TODO look at todo in job for moveToNextMachine
            if (theJob != null && !theJob.moveToNextMachine(timeNow, timeLimit)){
                numJobs--;
            }
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (Machine theMachine:machines.values()) {
            System.out.println("Machine " + theMachine.machineIndex + " completed " + theMachine.numTasks + " tasks");
            System.out.println("The total wait time was " + theMachine.totalWait + "\n");
        }
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        timeLimit = Integer.MAX_VALUE;
        inputData(); 
        startShop();
        simulate(); 
        outputStatistics();
    }

	private static Machine getTheMachine(int machineId){
	    return machines.get(machineId);
	}

}
