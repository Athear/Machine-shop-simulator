package applications;

import dataStructures.LinkedQueue;
import applications.Job;

public class Machine {
	public LinkedQueue jobQ;
	public int changeTime;
	public int totalWait;
	public int numTasks;
	public Job activeJob;
	int finishTime;
	int machineIndex;

	public Machine(int index, int maxFinishTime, int changeOverTime) {
		jobQ = new LinkedQueue();
		machineIndex=index;
		finishTime = maxFinishTime;
		changeTime = changeOverTime;
	}

	/**
	 * change the state of theMachine
	 * 
	 * @return last job run on this machine
	 * 
	 */

	//    static Machine getMachineFromArr(int machineAddress){   
	//        return machine[machineAddress];
	//    }


	public Job changeState(int currentTime, int maxTime) {// Task on theMachine has
		// finished,
		// schedule next one.
		Job lastJob;
		if (activeJob == null) {// in idle or change-over state
			lastJob = null;
			// wait over, ready for new job
			if (jobQ.isEmpty()){ // no waiting job
				finishTime=maxTime;
			}else {// take job off the queue and work on it
				activeJob = (Job) jobQ.remove();
				totalWait += currentTime - activeJob.arrivalTime;
				numTasks++;
				int t = activeJob.removeNextTask();
				finishTime=currentTime + t;
			}
		} else {// task has just finished on machine[theMachine]
			// schedule change-over time
			lastJob = activeJob;
			activeJob = null;
			finishTime=currentTime + changeTime;
		}

		return lastJob;
	}

	public int nextEventTime() {
		return finishTime;
	}
}