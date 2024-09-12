package machine;

import Process.*;
import java.util.HashMap;
import items.*;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;

public class Machine { //Classe que representa a máquina que executa os processos
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
	
	private void initInstrucions(HashMap<String, Commands> list) { //Estrutura auxiliar para mapear os comandos suportados
		list.put("X=0", Commands.ATTRIBUTION);
		list.put("Y=0", Commands.ATTRIBUTION);
		list.put("E/S", Commands.IO);
		list.put("COM", Commands.COM);
		list.put("SAIDA", Commands.EXIT);
	}
	
	private Commands getID(String instruction) { //Retorna o identificador da instrução atual
		if((instruction.charAt(0) == 'X') || (instruction.charAt(0) == 'Y')) {
			return ListInstructions.get("X=0");
		}
		return ListInstructions.get(instruction);
	}
	
	public static Machine getInstance() { //Design Patterns Siglenton
		if(obj == null) {
			obj = new Machine();
		}
		return obj;
	}
	
	public boolean areThereProcess() { //Verifica se há processos para executar
		return ready.areThereProcess() || blocked.areThereProcess();
	}
	
	public void execute() { //Executa o processo
		BCP process = ready.pick(); // Pega o processo prioritário
		verifyBlocked(); // Desconta os créditos dos processos bloqueados
		if(process != null)
		{	int i = 0;
			boolean flagID = true;
			while(process.getTime() > 0 && (process.getState() == StateProcess.READY)) { //Verifica se processo pode ser executado
				String instruction = process.getInstruction(); //Pega a instrução
				Commands ID = getID(instruction);
				process.setState(StateProcess.RUNNING); //Roda a instrução
				if(i == 0) writer.write("Executando " + process.getName()); 
				i++;
				switch(ID) {
					case Commands.ATTRIBUTION: //Atribuição
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
					case Commands.IO: //Entrada e Saída
						process.setState(StateProcess.BLOCKED); //Bloqueia o processo
						blocked.add(process);
						writer.write("E/S iniciada em " + process.getName());
						if(i == 1) {
							writer.write("Interrompendo " + process.getName() + " após " + i + " instrução");
						}
						else {
							writer.write("Interrompendo " + process.getName() + " após " + i + " instruções");
						}
						flagID = false;
						break;
					case Commands.COM: //Comando
						process.setState(StateProcess.READY);
						break;
					case Commands.EXIT: //Fim de programa
						process.setState(StateProcess.FINISHED);
						writer.write(process.getName() + " terminado. X=" + process.getX() + ". Y=" + process.getY());
						table.remove(process); //Mata o processo
						flagID = false;
						break;
				}
				process.reduceTime(); //Reduz o tempo de execução
			
			}
			process.reduceCredit(); //Reduz os créditos do processo
			process.restartTime(); //Restaura o tempo do processo para o quantum
			if(flagID) writer.write("Interrompendo " + process.getName() + " após " + i + " instruções");
			if(process.getState() == StateProcess.READY) ready.add(process); //Adiciona na fila de prontos se o processo não foi bloqueado
			cache.addQuantum(i); //Adiciona a quantidade de instruções executadas
			cache.addSwap(1); //Insera a troca de processo
			System.out.println("Yes");
		}
	}
	
	public void verifyBlocked() { 
		BCP p;
		Queue<BCP> queue = new LinkedList<>();
		while((p = blocked.pick()) != null) {
			p.removeWait(); //Decrementa a espera
			if(p.getWait() == 0) { //Desbloqueia o processo
				p.setState(StateProcess.READY);
				p.restartWait(); //Restaura o tempo de espera
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
	
	public boolean isZeroPriority() { //Verifica se todos os processos têm créditos nulos 
		boolean flag = true;
		List<BCP> queue = new ArrayList<>();
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
	
	public void creditRedistribution() { //Redistribui os créditos caso os créditos atuais são nulos
		List<BCP> queue = new ArrayList<>();
		while(ready.areThereProcess()) {
			BCP p = ready.pick();
			p.restart();
			queue.add(p);
		}
		for(BCP process: queue) {
			ready.add(process);
		}
	}
	
	public void loadProcess(String name, int number) { //Carrega os BCPs na memória da máquina
		try{
			List<String> list = reader.readFile(name);
			int p = Integer.parseInt(priority.get(number - 1));
			BCP process = new BCP(list, p, quantum);
			table.add(process); //Coloca BCP na tabela de processos
			ready.add(process); //Coloca BCP na fila de prontos
		}
		catch(IOException e) {
			System.out.println("Error file!");
		}
	}
	
	public void printLoading() { //Imprime o estado da fila de prontos
		List<BCP> queue = new ArrayList<>();
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
	
	public void exit() { //Encerra a máquina
		writer.close();
	}
		
}
