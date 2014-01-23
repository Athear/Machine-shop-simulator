package applications;

import applications.Task;
import dataStructures.LinkedQueue;

import applications.MachineShopSimulator;

public class Job {

	// data members
	public LinkedQueue taskQ; // this job's tasks
	public int length; // sum of scheduled task times
	public int arrivalTime; // arrival time at current queue
	public int id; // job identifier

	// constructor
	public Job(int theId) {
		id = theId;
		taskQ = new LinkedQueue();
		// length and arrivalTime have default value 0
	}

	// other methods
	public void addTask(int theMachine, int theTime) {
		taskQ.put(new Task(theMachine, theTime));
	}

	/**
	 * remove next task of job and return its time also update length
	 */
	public int removeNextTask() {
		int theTime = ((Task) taskQ.remove()).time;
		length += theTime;
		return theTime;
	}
	
	
	/*TODO:change taskQ to accept machines. This will remove need for the machine array and redundant arrays.
	 *TODO:Remove machine array from input variables.
	 *TODO:remove references to MachineShopSimulator. This should come with the other changes.
	 */
    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
	public boolean moveToNextMachine(int timeNow, Machine[] machine) {
        if (taskQ.isEmpty()) {// no next task
            System.out.println("Job " + id + " has completed at "
                    + timeNow + " Total wait was " + (timeNow - length));
            return false;
        } else {// theJob has a next task
                // get machine for next task
            int p = ((Task) taskQ.getFrontElement()).machine;
            // put on machine p's wait queue
            machine[p].jobQ.put(this);
            arrivalTime = timeNow;
            // if p idle, schedule immediately
            if (MachineShopSimulator.eList.nextEventTime(p) == MachineShopSimulator.largeTime) {// machine is idle
            	MachineShopSimulator.changeState(p);
            }
            return true;
        }
    }
}

