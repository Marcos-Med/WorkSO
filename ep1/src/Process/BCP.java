package Process;

import java.util.List;
import java.util.Objects;

public class BCP implements Comparable<BCP>{
	private List<String> textMemory;
	private StateProcess state;
	private final int priority;
	private int credit;
	private final int quantum;
	private int time;
	private int registerX;
	private int registerY;
	private String name;
	private int ProgramCounter;
	private int wait;
	
	public BCP(List<String> textMemory, int priority, int quantum) {
		name = textMemory.removeFirst();
		this.textMemory = textMemory;
		this.priority = priority;
		this.quantum = quantum;
		time = this.quantum;
		credit = this.priority;
		ProgramCounter = 0;
		registerX = 0;
		registerY = 0;
		state = StateProcess.READY;
		wait = 2;
	}
	
	@Override
	public int compareTo(BCP process) {
		return Integer.compare(this.credit, process.credit());
	}
	
	public int getX() {
		return registerX;
	}
	
	public int getY() {
		return registerY;
	}
	
	public void setX(int x) {
		registerX = x;
	}
	
	public void setY(int y) {
		registerY = y;
	}
	
	public StateProcess getState() {
		return state;
	}
	
	public void setState(StateProcess state) {
		this.state = state;
	}
	
	public String getName() {
		return name;
	}
	
	public void restart() {
		credit = priority;
	}
	
	public void reduceCredit() {
		if (credit > 0) credit--;
	}
	
	private void nextInstruction() {
		if(ProgramCounter < 21) ProgramCounter++;
	}
	
	public String getInstruction() {
		String instruction = textMemory.get(ProgramCounter);
		nextInstruction();
		return instruction;
	}
	
	public void reduceTime() {
		if(time > 0) time--;
	}
	
	public void restartTime() {
		time = quantum;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getWait() {
		return wait;
	}
	
	public void removeWait() {
		wait--;
	}
	
	public void restartWait() {
		wait = 2;
	}
	
	public int credit() {
		return this.credit;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj;
	}
}
