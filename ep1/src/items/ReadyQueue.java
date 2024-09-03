package items;

import java.util.Queue;
import java.util.PriorityQueue;
import Process.BCP;
import java.util.Collections;

public class ReadyQueue implements QueueAPP { //Classe que representa a fila de prontos
	private Queue<BCP> queue;
	private static QueueAPP obj;
	
	private ReadyQueue() {
		queue = new PriorityQueue<>(Collections.reverseOrder()); //Fila de prioridade onde o maior número
																// possui a maior prioridade
	}
	
	public static QueueAPP getInstance() { //Design Patterns Siglenton
		if(obj == null) {
			obj = new ReadyQueue();
		}
		return obj;
	}
	
	public void add(BCP process) { //Adiciona na fila de prontos
		queue.add(process);
	}
	
	public BCP pick() { //Retira da fila de prontos
		Object obj = queue.poll();
		return obj != null ? (BCP) obj : null;
	}
	
	public boolean areThereProcess() { //Verifica se há processos na fila de prontos
		return queue.size() > 0;
	}
}
