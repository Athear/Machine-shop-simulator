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

	public Machine(int index, int maxFinishTime) {
		jobQ = new LinkedQueue();
		machineIndex=index;
		finishTime = maxFinishTime;
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
        
	
        public Job changeState() {// Task on theMachine has
                                                    // finished,
            // schedule next one.
            Job lastJob;
            if (this.activeJob == null) {// in idle or change-over
                                               // state
                lastJob = null;
                // wait over, ready for new job
                if (this.jobQ.isEmpty()){ // no waiting job
                    finishTime=MachineShopSimulator.getLargeTime();
                }else {// take job off the queue and work on it
                    this.activeJob = (Job) this.jobQ.remove();
                    this.totalWait += MachineShopSimulator.getTimeNow() - this.activeJob.arrivalTime;
                    this.numTasks++;
                    int t = this.activeJob.removeNextTask();
                    finishTime=MachineShopSimulator.getTimeNow() + t;
                }
            } else {// task has just finished on machine[theMachine]
                    // schedule change-over time
                lastJob = this.activeJob;
                this.activeJob = null;
                finishTime=MachineShopSimulator.getTimeNow() + this.changeTime;
            }
    
            return lastJob;
        }

    public int nextEventTime() {
        return finishTime;
    }
}