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
	 * Change the state of theMachine:
	 * Start change-over if a task has just finished, or
	 * schedule the next waiting task once change-over is complete
	 * 
	 * @return last job run on this machine
	 */

	Job changeState(int currentTime, int maxTime) {
		Job lastJob;
		if (activeJob == null) {//change-over is complete
			lastJob = null;
			if (jobQ.isEmpty()){
				finishTime=maxTime;
			}else {
				activeJob = (Job) jobQ.remove();
				totalWait += currentTime - activeJob.arrivalTime;
				numTasks++;
				int t = activeJob.removeNextTask();
				finishTime=currentTime + t;
			}
		} else {// a task has just finished on this machine 
			
			lastJob = activeJob;
			activeJob = null;
			finishTime=currentTime + changeTime;
		}

		return lastJob;
	}

	public int timeUntilFinished() {
		return finishTime;
	}
}