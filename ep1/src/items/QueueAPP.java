package items;

import Process.BCP;

public interface QueueAPP { //Interface de filas da aplicação
	public abstract void add(BCP process);
	public abstract BCP pick();
	public abstract boolean areThereProcess();
}
