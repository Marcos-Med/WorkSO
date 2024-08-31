package items;

import java.util.Queue;
import java.util.LinkedList;
import Process.BCP;


public class BlockedQueue implements QueueAPP {
	private Queue<BCP> queue;
	private static QueueAPP obj;
	
	private BlockedQueue() {
		queue = new LinkedList<>();
	}
	
	public static QueueAPP getInstance() {
		if(obj == null) {
			obj = new BlockedQueue();
		}
		
		return obj;
	}
	
	public void add(BCP process){
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
