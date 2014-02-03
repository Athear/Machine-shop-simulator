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

	public Machine(int index) {
		jobQ = new LinkedQueue();
		machineIndex=index;
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
        
	
	//TODO get rid of address
        public Job changeState() {// Task on theMachine has
                                                    // finished,
            // schedule next one.
            Job lastJob;
            if (this.activeJob == null) {// in idle or change-over
                                               // state
                lastJob = null;
                // wait over, ready for new job
                if (this.jobQ.isEmpty()){ // no waiting job
                    MachineShopSimulator.geteList().setFinishTime(this, MachineShopSimulator.getLargeTime());
                }else {// take job off the queue and work on it
                    this.activeJob = (Job) this.jobQ.remove();
                    this.totalWait += MachineShopSimulator.getTimeNow() - this.activeJob.arrivalTime;
                    this.numTasks++;
                    int t = this.activeJob.removeNextTask();
                    MachineShopSimulator.geteList().setFinishTime(this, MachineShopSimulator.getTimeNow() + t);
                }
            } else {// task has just finished on machine[theMachine]
                    // schedule change-over time
                lastJob = this.activeJob;
                this.activeJob = null;
                MachineShopSimulator.geteList().setFinishTime(this, MachineShopSimulator.getTimeNow() + this.changeTime);
            }
    
            return lastJob;
        }
}