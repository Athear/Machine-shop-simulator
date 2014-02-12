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
    public void addTask(Machine theMachine, int theTime) {
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
    boolean moveToNextMachine(int currentTime, int maxTime) {
        if (taskQ.isEmpty()) {// no next task //TODO: WTF split this. Make one a boolean and the other void.
            System.out.print("Job " + id + " has completed at " + currentTime);
            System.out.println(" Total wait was " + (currentTime - length));
            return false;
        }// theJob has a next task
         // get machine for next task
        Machine nextMachine = ((Task) taskQ.getFrontElement()).machine;
        
        // put on machine p's wait queue
        nextMachine.jobQ.put(this);
        arrivalTime = currentTime;
        
        if (nextMachine.timeUntilFinished() == maxTime) {// machine is idle
            nextMachine.changeState(currentTime, maxTime);
        }
        return true;
        
    }
}
