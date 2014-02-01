/** machine shop simulation */

package applications;

import utilities.MyInputStream;
import dataStructures.LinkedQueue;
import exceptions.MyInputException;
import applications.Machine;

public class MachineShopSimulator {
    
    
    public static final String NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1 = "number of machines and jobs must be >= 1";
    public static final String CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0 = "change-over time must be >= 0";
    public static final String EACH_JOB_MUST_HAVE_AT_LEAST_1_TASK = "each job must have >= 1 task";
    public static final String BAD_MACHINE_NUMBER_OR_TASK_TIME = "bad machine number or task time";


    // data members of MachineShopSimulator
    static int timeNow; // current time
    private static int numMachines; // number of machines
    private static int numJobs; // number of jobs
    static EventList eList; // pointer to event list
    static Machine[] machine; // array of machines
    static int largeTime; // all machines finish before this

    /** input machine shop data */
    static void inputData() {
        // define the input stream to be the standard input stream
        MyInputStream keyboard = new MyInputStream();

        System.out.println("Enter number of machines and jobs");
        setNumMachines(keyboard.readInteger());
        numJobs = keyboard.readInteger();
        if (getNumMachines() < 1 || numJobs < 1)
            throw new MyInputException(NUMBER_OF_MACHINES_AND_JOBS_MUST_BE_AT_LEAST_1);

        // create event and machine queues
        eList = new EventList(getNumMachines(), largeTime);
        machine = new Machine[getNumMachines() + 1];
        for (int i = 1; i <= getNumMachines(); i++)
            machine[i] = new Machine();

        // input the change-over times
        System.out.println("Enter change-over times for machines");
        for (int j = 1; j <= getNumMachines(); j++) {
            int ct = keyboard.readInteger();
            if (ct < 0)
                throw new MyInputException(CHANGE_OVER_TIME_MUST_BE_AT_LEAST_0);
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
                if (theMachine < 1 || theMachine > getNumMachines()
                        || theTaskTime < 1)
                    throw new MyInputException(BAD_MACHINE_NUMBER_OR_TASK_TIME);
                if (j == 1)
                    firstMachine = theMachine; // job's first machine
                theJob.addTask(theMachine, theTaskTime); // add to
            } // task queue
            machine[firstMachine].jobQ.put(theJob);
        }
    }

    /** load first jobs onto each machine */
    static void startShop() {
        for (int p = 1; p <= getNumMachines(); p++)
            getTheMachine(p).changeState(p);
    }

    /** process all jobs to completion */
    static void simulate() {
        while (numJobs > 0) {// at least one job left
            int nextToFinish = eList.nextEventMachine();
            timeNow = eList.nextEventTime(nextToFinish);
            // change job on machine nextToFinish
            Job theJob = getTheMachine(nextToFinish).changeState(nextToFinish);
            // move theJob to its next machine
            // decrement numJobs if theJob has finished
            if (theJob != null && !theJob.moveToNextMachine())
                numJobs--;
        }
    }

    /** output wait times at machines */
    static void outputStatistics() {
        System.out.println("Finish time = " + timeNow);
        for (int p = 1; p <= getNumMachines(); p++) {
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
        /*
         * It's vital that we (re)set this to 0 because if the simulator is called
         * multiple times (as happens in the acceptance tests), because timeNow
         * is static it ends up carrying over from the last time it was run. I'm
         * not convinced this is the best place for this to happen, though.
         */
        timeNow = 0;
        inputData(); // get machine and job data
        startShop(); // initial machine loading
        simulate(); // run all jobs through shop
        outputStatistics(); // output machine wait times
    }

	static int getTimeNow() {
		return timeNow;
	}

	static void setTimeNow(int timeNow) {
		MachineShopSimulator.timeNow = timeNow;
	}

	static int getLargeTime() {
		return largeTime;
	}

	static void setLargeTime(int largeTime) {
		MachineShopSimulator.largeTime = largeTime;
	}

	static EventList geteList() {
		return eList;
	}

	static void seteList(EventList eList) {
		MachineShopSimulator.eList = eList;
	}

	static Machine getTheMachine(int arrAddress){
	    return getMachine()[arrAddress];
	}
	
	
	static Machine[] getMachine() {
		return machine;
	}

	static void setMachine(Machine[] machine) {
		MachineShopSimulator.machine = machine;
	}

    static int getNumMachines() {
        return numMachines;
    }

    static void setNumMachines(int numMachines) {
        MachineShopSimulator.numMachines = numMachines;
    }
}
