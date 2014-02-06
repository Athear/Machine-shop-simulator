package applications;


public class Task {
	// data members
	public Machine machine;
	public int machineIndex;
	public int time;

	// constructor
	public Task(int theMachine, int theTime) {
		machineIndex = theMachine;
		time = theTime;
	}
	
	public Task(Machine theMachine, int theTime){
		machine = theMachine;
		machineIndex = theMachine.machineIndex;
		time = theTime;
	}
}

