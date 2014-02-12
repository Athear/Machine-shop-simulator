/** machine shop simulation */

package applications;

import utilities.MyInputStream;
import exceptions.MyInputException;
import applications.Machine;

public class MachineShopSimulator {
    
    
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";


    // data members of MachineShopSimulator
    private static int timeNow; // current time //TODO: rename runTime or shopRunTime?
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    private static Machine[] machine; // array of machines
    private static int timeLimit; // all machines finish before this

    
     
    private static Machine nextFreeMachine(){
    	Machine currentShortest = getTheMachine(1);
    	Machine nextToCheck;
    	for (int i = 2; i <= numMachines; i++){
    		nextToCheck = getTheMachine(i);
            if (nextToCheck.finishTime < currentShortest.finishTime){// i finishes earlier
            	currentShortest = nextToCheck;
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
        if (numMachines < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);

        // create the machine queues
        createMachines(keyboard);

        // input the jobs
         createJobs(keyboard);
    }

	private static void createMachines(MyInputStream keyboard) {
		machine = new Machine[numMachines + 1];
        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= numMachines; j++) {
            int changeTime = keyboard.readInteger();
            if (changeTime < 0){
                throw new MyInputException(CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0);
            }
            machine[j] = new Machine(j,timeLimit, changeTime);
        }
	}

	private static void createJobs(MyInputStream keyboard) {
		Job theJob;
		for (int i = 1; i <= numJobs; i++) {
            System.out.println("Enter number of tasks for job " + i);
            int tasks = keyboard.readInteger(); // number of tasks
            if (tasks < 1){
                throw new MyInputException(EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK);
            }
            // create the job
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)" + " in process order");
            for (int j = 1; j <= tasks; j++) {// get tasks for job i
                int theMachineIndex = keyboard.readInteger();
                int theTaskTime = keyboard.readInteger();
                if (theMachineIndex < 1 || theMachineIndex > numMachines || theTaskTime < 1){
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                }
                Machine theMachine = getTheMachine(theMachineIndex);
                if (j == 1){
                	theMachine.jobQ.put(theJob); // set the first machine for the job
                }
                theJob.addTask(theMachine, theTaskTime); // add to
            } // task queue
            
        }
	}

    /** load first jobs onto each machine */
    private static void startShop() {
    	timeNow = 0;
        for (int p = 1; p <= numMachines; p++)
            getTheMachine(p).changeState(timeNow, timeLimit);
    }

    /** process all jobs to completion */
    private static void simulate() {
        while (numJobs > 0) {// at least one job left
            Machine nextToFinish = nextFreeMachine();
            timeNow = nextToFinish.nextEventTime();
            // change job on machine nextToFinish
            Job theJob = nextToFinish.changeState(timeNow, timeLimit);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !theJob.moveToNextMachine(timeNow, timeLimit)){
                numJobs--;
            }
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 1; p <= numMachines; p++) {
        	Machine theMachine = getTheMachine(p);
            System.out.println("Machine " + p + " completed " + theMachine.numTasks + " tasks");
            System.out.println("The total wait time was " + theMachine.totalWait + "\n");
        }
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        timeLimit = Integer.MAX_VALUE;
        inputData(); // get machine and job data
        startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        outputStatistics(); // output machine wait times
    }

	private static Machine getTheMachine(int arrAddress){
	    return machine[arrAddress];
	}

}
