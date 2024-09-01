package machine;

import java.util.List;
import java.util.ArrayList;

public class Cache <T> {
	private List<T> quantum;
	private List<T> swaps;
	
	public Cache() {
		quantum = new ArrayList<>();
		swaps = new ArrayList<>();
	}
	
	public void addQuantum(T e) {
		quantum.add(e);
	}
	
	public void addSwap(T e) {
		swaps.add(e);
	}
	
	public List<T> removeQuantum() {
		List<T> q = quantum;
		quantum = new ArrayList<>();
		return q;
	}
	
	public List<T> removeSwap() {
		List<T> s = swaps;
		swaps = new ArrayList<>();
		return s;
	}
}
