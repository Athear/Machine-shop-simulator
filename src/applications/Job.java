package applications;

import applications.Task;
import dataStructures.LinkedQueue;

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

	// methods
	/**
	 * move theJob to machine for its next task
	 * 
	 * @return false iff no next task
	 */
	boolean moveToNextMachine() {
	    if (taskQ.isEmpty()) {// no next task
	        System.out.println("Job " + id + " has completed at "
	                + MachineShopSimulator.getTimeNow() + " Total wait was " + (MachineShopSimulator.getTimeNow() - length));
	        return false;
	    } else {// theJob has a next task
	            // get machine for next task
	        int p = ((Task) taskQ.getFrontElement()).machine;
	        // put on machine p's wait queue
	        MachineShopSimulator.getMachine()[p].jobQ.put(this);
	        arrivalTime = MachineShopSimulator.getTimeNow();
	        // if p idle, schedule immediately
	        if (MachineShopSimulator.geteList().nextEventTime(p) == MachineShopSimulator.getLargeTime()) {// machine is idle
	            Machine.changeState(p);
	        }
	        return true;
	    }
	}
}

