package applications;

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



    public int nextEventTime(int theMachine) {
        return finishTime[theMachine];
    }

    public void setFinishTime(Machine theMachine, int theTime) {
        finishTime[theMachine.machineIndex] = theTime;
        theMachine.finishTime = theTime;
    }
}
