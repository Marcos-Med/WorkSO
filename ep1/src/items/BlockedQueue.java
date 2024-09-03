package items;

import java.util.Queue;
import java.util.LinkedList;
import Process.BCP;


public class BlockedQueue implements QueueAPP { //Classe que representa a fila de bloqueados
	private Queue<BCP> queue;
	private static QueueAPP obj;
	
	private BlockedQueue() {
		queue = new LinkedList<>();
	}
	
	public static QueueAPP getInstance() { //Implementação da Design Patterns Siglenton
		if(obj == null) {
			obj = new BlockedQueue();
		}
		
		return obj;
	}
	
	public void add(BCP process){ //Insere na fila de bloqueados
		queue.add(process);
	}
	
	public BCP pick() { //Retira da fila de bloqueados
		Object obj = queue.poll();
		return obj != null ? (BCP) obj : null;
	}
	
	public boolean areThereProcess() { //Verifica se há processos bloqueados
		return queue.size() > 0;
	}
}
