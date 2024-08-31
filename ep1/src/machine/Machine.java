package machine;

import Process.*;
import java.util.HashMap;
import items.*;
import java.util.List;
import java.io.IOException;

public class Machine {
	private static Machine obj; 
	private HashMap<String, Commands> ListInstructions;
	private TableProcess table;
	private QueueAPP blocked;
	private QueueAPP ready;
	private Reader reader;
	private List<String> priority;
	private int quantum;
	
	private Machine() {
		ListInstructions = new HashMap<>();
		blocked = BlockedQueue.getInstance();
		ready = ReadyQueue.getInstance();
		table = TableProcess.getInstance();
		reader = ReaderFileOS.getInstance();
		try{
			priority = reader.readFile("../programas/prioridades.txt");
			quantum  = reader.readFile("../programas/quantum.txt").get(0).charAt(0);
		}
		catch(IOException e) {
			System.out.println("Error file");
		}
	}
	
	public static Machine getInstance() {
		if(obj == null) {
			obj = new Machine();
		}
		return obj;
	}
	
	public boolean areThereProcess() {
		return ready.areThereProcess() || blocked.areThereProcess();
	}
	
	public void execute() {
		BCP process = ready.pick();
		while(process.getTime() > 0 && (process.getState() == StateProcess.READY)) {
			String instruction = process.getInstruction();
			Commands ID = ListInstructions.get(instruction);
			process.setState(StateProcess.RUNNING);
			switch(ID) {
				case Commands.ATTRIBUTION:
					if(instruction.contains("X")) {
						process.setX(instruction.charAt(instruction.length() - 1));
					}
					else {
						process.setY(instruction.charAt(instruction.length() - 1));
					}
					process.setState(StateProcess.READY);
					break;
				case Commands.IO:
					process.setState(StateProcess.BLOCKED);
					blocked.add(process);
					break;
				case Commands.COM:
					process.setState(StateProcess.READY);
					break;
				case Commands.EXIT:
					process.setState(StateProcess.FINISHED);
					table.remove(process);
					break;
			}
			if(ListInstructions.containsValue(ID)) process.reduceTime();
			
		}
		process.reduceCredit();
		process.restartTime();
		if(process.getState() == StateProcess.READY) ready.add(process);
	}
	
	public void loadProcess(String name, int number) {
		try{
			List<String> list = reader.readFile(name);
			int p = priority.get(number - 1).charAt(0);
			BCP process = new BCP(list, p, quantum);
			table.add(process);
			ready.add(process);
		}
		catch(IOException e) {
			System.out.println("Error file!");
		}
	}
		
}
