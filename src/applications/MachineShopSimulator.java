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
    private static int largeTime; // all machines finish before this //TODO: Rename maxTime or timeLimit? //TODO: constant or potential variable?

    
    
    public static int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int p = 1;
        int t = machine[1].finishTime;
        for (int i = 2; i < machine.length; i++){
            if (machine[i].finishTime < t) {// i finishes earlier
                p = i;
                t = machine[i].finishTime;
            }
        }
        return p;
    }
    
    public static Machine nextFreeMachine(){
    	Machine currentShortest = getTheMachine(1);
    	Machine nextToCheck;
    	for (int i = 2; i < machine.length; i++){
    		nextToCheck = getTheMachine(i);
            if (nextToCheck.finishTime < currentShortest.finishTime){// i finishes earlier
            	currentShortest = nextToCheck;
            }
    	}
		return currentShortest;
    }
    
    
    
    
    
    
    /** input machine shop data */
    static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        numMachines = (keyboard.readInteger());
        numJobs = keyboard.readInteger();
        if (numMachines < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);

        // create event and machine queues
        machine = new Machine[numMachines + 1];

        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= numMachines; j++) {
            int ct = keyboard.readInteger();
            if (ct < 0)
                throw new MyInputException(CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0);
            machine[j] = new Machine(j,largeTime);
            machine[j].changeTime = ct;
        }

        // input the jobs
        Job theJob;
        for (int i = 1; i <= numJobs; i++) {
            System.out.println("Enter number of tasks for job " + i);
            int tasks = keyboard.readInteger(); // number of tasks
            int firstMachine = 0; // machine for first task
            if (tasks < 1)
                throw new MyInputException(EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK);

            // create the job
            theJob = new Job(i);
            System.out.println("Enter the tasks (machine, time)"
                    + " in process order");
            for (int j = 1; j <= tasks; j++) {// get tasks for job i
                int theMachine = keyboard.readInteger();
                int theTaskTime = keyboard.readInteger();
                if (theMachine < 1 || theMachine > numMachines || theTaskTime < 1)
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                if (j == 1)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(getTheMachine(theMachine), theTaskTime); // add to
            } // task queue
            getTheMachine(firstMachine).jobQ.put(theJob);
        }
    }

    /** load first jobs onto each machine */
    static void startShop() {
    	timeNow = 0;
        for (int p = 1; p <= numMachines; p++)
            getTheMachine(p).changeState(timeNow, largeTime);
    }

    /** process all jobs to completion */
    static void simulate() {
        while (numJobs > 0) {// at least one job left
            Machine nextToFinish = machine[nextEventMachine()];
            timeNow = nextToFinish.nextEventTime();
            // change job on machine nextToFinish
            Job theJob = nextToFinish.changeState(timeNow, largeTime);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !theJob.moveToNextMachine(timeNow, largeTime))
                numJobs--;
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 1; p <= numMachines; p++) {
            System.out.println("Machine " + p + " completed "
                    + machine[p].numTasks + " tasks");
            System.out.println("The total wait time was "
                    + machine[p].totalWait);
            System.out.println();
        }
    }

    /** entry point for machine shop simulator */
    public static void main(String[] args) {
        largeTime = Integer.MAX_VALUE;
        inputData(); // get machine and job data
        startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        outputStatistics(); // output machine wait times
    }

	static Machine getTheMachine(int arrAddress){
	    return machine[arrAddress];
	}

}
