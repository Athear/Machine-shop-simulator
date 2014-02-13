package applications;

import applications.Task;
import dataStructures.LinkedQueue;

public class Job {

    public LinkedQueue taskQ; 
    public int totalJobTime; 
    public int arrivalTime; // arrival time at current queue
    public int id;

    public Job(int theId) {
        id = theId;
        taskQ = new LinkedQueue();
    }

    public void addTask(Machine theMachine, int theTime) {
        taskQ.put(new Task(theMachine, theTime));
    }

    /**
     * remove next task of job and return its time also update length
     */
    public int removeNextTask() {
        int theTime = ((Task) taskQ.remove()).time;
        totalJobTime += theTime;
        return theTime;
    }

    /**
     * move theJob to machine for its next task
     * 
     * @return false iff no next task
     */
    boolean moveToNextMachine(int currentTime, int maxTime) {
        if (taskQ.isEmpty()) { //TODO: Split this. Make one a boolean and the other void.
            System.out.print("Job " + id + " has completed at " + currentTime);
            System.out.println(" Total wait was " + (currentTime - totalJobTime));
            return false;
        }
        
        Machine nextMachine = ((Task) taskQ.getFrontElement()).machine;
        
        //add "this" job to the nextMachines job queue
        nextMachine.jobQ.put(this);
        arrivalTime = currentTime;
        
        if (nextMachine.timeUntilFinished() == maxTime) {// machine is idle
            nextMachine.changeState(currentTime, maxTime);
        }
        return true;
        
    }
}
