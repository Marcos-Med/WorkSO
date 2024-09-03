package items;

import java.util.Queue;
import java.util.PriorityQueue;
import Process.BCP;
import java.util.Collections;

public class ReadyQueue implements QueueAPP {
	private Queue<BCP> queue;
	private static QueueAPP obj;
	
	private ReadyQueue() {
		queue = new PriorityQueue<>(Collections.reverseOrder());
	}
	
	public static QueueAPP getInstance() {
		if(obj == null) {
			obj = new ReadyQueue();
		}
		return obj;
	}
	
	public void add(BCP process) {
		queue.add(process);
	}
	
	public BCP pick() {
		Object obj = queue.poll();
		return obj != null ? (BCP) obj : null;
	}
	
	public boolean areThereProcess() {
		return queue.size() > 0;
	}
}
