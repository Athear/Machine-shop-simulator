package applications;


//TODO: split functionality from this class between Machine and Task classes.
public class EventList {
    // data members
    int[] finishTime; // finish time array

    // constructor
    public EventList(int theNumMachines, int theLargeTime) {// initialize
                                                             // finish
                                                             // times for
                                                             // m
                                                             // machines
        if (theNumMachines < 1)
            throw new IllegalArgumentException("number of machines must be >= 1");
        finishTime = new int[theNumMachines + 1];

        // all machines are idle, initialize with
        // large finish time
        for (int i = 1; i <= theNumMachines; i++)
            finishTime[i] = theLargeTime;
    }

    //TODO:move this method into the simluator proper. 
    /** @return machine for next event */
    public int nextEventMachine() {
        // find first machine to finish, this is the
        // machine with smallest finish time
        int p = 1;
        int t = finishTime[1];
        for (int i = 2; i < finishTime.length; i++)
            if (finishTime[i] < t) {// i finishes earlier
                p = i;
                t = finishTime[i];
            }
        return p;
    }

    public int nextEventTime(int theMachine) {
        return finishTime[theMachine];
    }
    
    //TODO: change this so it takes a machine. Get rid of this stupid array.
    public void setFinishTime(int theMachine, int theTime) {
        finishTime[theMachine] = theTime;
    }
}
