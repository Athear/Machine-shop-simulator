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
        
        static Job changeState(int machineAddress) {// Task on theMachine has
                                                    // finished,
            // schedule next one.
            Job lastJob;
            Machine theMachine = MachineShopSimulator.getMachine()[machineAddress];
            if (theMachine.activeJob == null) {// in idle or change-over
                                               // state
                lastJob = null;
                // wait over, ready for new job
                if (theMachine.jobQ.isEmpty()){ // no waiting job
                    MachineShopSimulator.geteList().setFinishTime(machineAddress, MachineShopSimulator.getLargeTime());
                }else {// take job off the queue and work on it
                    theMachine.activeJob = (Job) theMachine.jobQ.remove();
                    theMachine.totalWait += MachineShopSimulator.getTimeNow()
                            - theMachine.activeJob.arrivalTime;
                    theMachine.numTasks++;
                    int t = theMachine.activeJob.removeNextTask();
                    MachineShopSimulator.geteList().setFinishTime(machineAddress, MachineShopSimulator.getTimeNow() + t);
                }
            } else {// task has just finished on machine[theMachine]
                    // schedule change-over time
                lastJob = theMachine.activeJob;
                theMachine.activeJob = null;
                MachineShopSimulator.geteList().setFinishTime(machineAddress, MachineShopSimulator.getTimeNow() + theMachine.changeTime);
            }
    
            return lastJob;
        }
}