package items;

import Process.BCP;
import java.util.List;
import java.util.ArrayList;

public class ReadyQueue implements QueueAPP { //Classe que representa a fila de prontos
	private List<BCP> queue;
	private static QueueAPP obj;
	
	private ReadyQueue() {
		//Fila de prioridade onde o maior número
		// possui a maior prioridade
		queue = new ArrayList<>();
	}
	
	public static QueueAPP getInstance() { //Design Patterns Siglenton
		if(obj == null) {
			obj = new ReadyQueue();
		}
		return obj;
	}
	
	public void add(BCP process) { //Adiciona na fila de prontos
		BCP p;
		int i;
		queue.add(process);
		for(i = queue.size() - 1; i > 0; i--) {
			p = queue.get(i-1);
			if(process.compareTo(p) >= 0) {
				queue.set(i, p);
				queue.set(i-1, process);
			}
		}
	}
	
	public BCP pick() { //Retira da fila de prontos
		BCP obj;
		try{
			obj = (BCP) queue.remove(0);
			return obj;
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public boolean areThereProcess() { //Verifica se há processos na fila de prontos
		return queue.size() > 0;
	}
}
