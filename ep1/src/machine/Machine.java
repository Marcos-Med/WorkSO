package machine;

import Process.*;
import java.util.HashMap;
import items.*;
import java.util.List;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.io.IOException;
import java.util.Collections;

public class Machine {
	private static Machine obj; 
	private HashMap<String, Commands> ListInstructions;
	private TableProcess table;
	private QueueAPP blocked;
	private QueueAPP ready;
	private Reader reader;
	private Write writer;
	private List<String> priority;
	private int quantum;
	private Cache<Integer> cache;
	
	private Machine() {
		ListInstructions = new HashMap<>();
		blocked = BlockedQueue.getInstance();
		ready = ReadyQueue.getInstance();
		table = TableProcess.getInstance();
		reader = ReaderFileOS.getInstance();
		cache = new Cache<>();
		try{
			priority = reader.readFile("src/programas/prioridades.txt");
			quantum  = Integer.parseInt(reader.readFile("src/programas/quantum.txt").get(0));
			writer = LogFile.getInstance();
		}
		catch(IOException e) {
			System.out.println("Error file");
		}
		
		initInstrucions(ListInstructions);
		
	}
	
	private void initInstrucions(HashMap<String, Commands> list) {
		list.put("X=0", Commands.ATTRIBUTION);
		list.put("Y=0", Commands.ATTRIBUTION);
		list.put("E/S", Commands.IO);
		list.put("COM", Commands.COM);
		list.put("SAIDA", Commands.EXIT);
	}
	
	private Commands getID(String instruction) {
		if((instruction.charAt(0) == 'X') || (instruction.charAt(0) == 'Y')) {
			return ListInstructions.get("X=0");
		}
		return ListInstructions.get(instruction);
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
		verifyBlocked();
		if(process != null)
		{	int i = 0;
			boolean flagID = true;
			while(process.getTime() > 0 && (process.getState() == StateProcess.READY)) {
				String instruction = process.getInstruction();
				Commands ID = getID(instruction);
				process.setState(StateProcess.RUNNING);
				if(i == 0) writer.write("Executando " + process.getName()); 
				i++;
				switch(ID) {
					case Commands.ATTRIBUTION:
						String number = "";
						for(int j = 2; j < instruction.length(); j++) {
							number += instruction.charAt(j);
						}
						if(instruction.contains("X")) {
							process.setX(Integer.parseInt(number));
						}
						else {
							process.setY(Integer.parseInt(number));
						}
						process.setState(StateProcess.READY);
						break;
					case Commands.IO:
						process.setState(StateProcess.BLOCKED);
						blocked.add(process);
						writer.write("E/S iniciada em " + process.getName());
						if(i == 1) {
							writer.write("Interrompendo " + process.getName() + " após " + i + " instrução (havia apenas a E/S)");
						}
						else {
							if(i-1 == 1) writer.write("Interrompendo " + process.getName() + " após " + i + " instruções (" + (i - 1) + " comando antes da E/S)");
							else writer.write("Interrompendo " + process.getName() + " após " + i + " instruções (" + (i - 1) + " comandos antes da E/S)");
						}
						flagID = false;
						break;
					case Commands.COM:
						process.setState(StateProcess.READY);
						break;
					case Commands.EXIT:
						process.setState(StateProcess.FINISHED);
						writer.write(process.getName() + " terminado. X=" + process.getX() + ". Y=" + process.getY());
						table.remove(process);
						flagID = false;
						break;
				}
				process.reduceTime();
			
			}
			process.reduceCredit();
			process.restartTime();
			if(flagID) writer.write("Interrompendo " + process.getName() + " após " + i + " instruções");
			if(process.getState() == StateProcess.READY) ready.add(process);
			cache.addQuantum(i);
			cache.addSwap(1);
		
		}
	}
	
	public void verifyBlocked() {
		BCP p;
		Queue<BCP> queue = new LinkedList<>();
		while((p = blocked.pick()) != null) {
			p.removeWait();
			if(p.getWait() == 0) {
				p.setState(StateProcess.READY);
				p.restartWait();
				ready.add(p);
			}
			else {
				queue.add(p);
			}
		}
		for(BCP pr: queue) {
			blocked.add(pr);
		}
	}
	
	public boolean isZeroPriority() {
		boolean flag = true;
		Queue<BCP> queue = new PriorityQueue<>(Collections.reverseOrder());
		while(ready.areThereProcess()) {
			BCP p = ready.pick();
			queue.add(p);
			if(p.credit() != 0) {
				flag = false;
				break;
			}
		}
		for(BCP process: queue) {
			ready.add(process);
		}
		return flag;
	}
	
	public void creditRedistribution() {
		Queue<BCP> queue = new PriorityQueue<>(Collections.reverseOrder());
		while(ready.areThereProcess()) {
			BCP p = ready.pick();
			p.restart();
			queue.add(p);
		}
		for(BCP process: queue) {
			ready.add(process);
		}
	}
	
	public void loadProcess(String name, int number) {
		try{
			List<String> list = reader.readFile(name);
			int p = Integer.parseInt(priority.get(number - 1));
			BCP process = new BCP(list, p, quantum);
			table.add(process);
			ready.add(process);
		}
		catch(IOException e) {
			System.out.println("Error file!");
		}
	}
	
	public void printLoading() {
		Queue<BCP> queue = new PriorityQueue<>(Collections.reverseOrder());
		while(ready.areThereProcess()) {
			BCP p = ready.pick();
			writer.write("Carregando " + p.getName());
			queue.add(p);
		}
		for(BCP process: queue) {
			ready.add(process);
		}
	}
	
	public List<Integer> returnQuantum(){
		return cache.removeQuantum();
	}
	
	public List<Integer> returnSwaps(){
		return cache.removeSwap();
	}
	
	public int getQuantum() {
		return quantum;
	}
	
	public void exit() {
		writer.close();
	}
		
}
