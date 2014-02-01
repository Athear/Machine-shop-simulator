package applications;

import dataStructures.LinkedQueue;
import applications.Job;

public class Machine {
	public LinkedQueue jobQ;
	public int changeTime;
	public int totalWait;
	public int numTasks;
	public Job activeJob;

	public Machine() {
		jobQ = new LinkedQueue();
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
        
	
	//TODO get rid of 
        public Job changeState(int address) {// Task on theMachine has
                                                    // finished,
            // schedule next one.
            Job lastJob;
            //Machine theMachine = MachineShopSimulator.getMachine()[machineAddress];
            if (this.activeJob == null) {// in idle or change-over
                                               // state
                lastJob = null;
                // wait over, ready for new job
                if (this.jobQ.isEmpty()){ // no waiting job
                    MachineShopSimulator.geteList().setFinishTime(address, MachineShopSimulator.getLargeTime());
                }else {// take job off the queue and work on it
                    this.activeJob = (Job) this.jobQ.remove();
                    this.totalWait += MachineShopSimulator.getTimeNow()
                            - this.activeJob.arrivalTime;
                    this.numTasks++;
                    int t = this.activeJob.removeNextTask();
                    MachineShopSimulator.geteList().setFinishTime(address, MachineShopSimulator.getTimeNow() + t);
                }
            } else {// task has just finished on machine[theMachine]
                    // schedule change-over time
                lastJob = this.activeJob;
                this.activeJob = null;
                MachineShopSimulator.geteList().setFinishTime(address, MachineShopSimulator.getTimeNow() + this.changeTime);
            }
    
            return lastJob;
        }
}