package items;

import Process.BCP;

public interface QueueAPP {
	public abstract void add(BCP process);
	public abstract BCP pick();
	public abstract boolean areThereProcess();
}
