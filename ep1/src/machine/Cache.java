package machine;

import java.util.List;
import java.util.ArrayList;

public class Cache <T> { //Classe que contem informações para os cálculos estatísticos
	private List<T> quantum;
	private List<T> swaps;
	
	public Cache() {
		quantum = new ArrayList<>();
		swaps = new ArrayList<>();
	}
	
	public void addQuantum(T e) { //Adiciona o quantidade de instruções utilizadas em cada quantum
		quantum.add(e);
	}
	
	public void addSwap(T e) { //Adiciona as trocas realizadas por cada processo
		swaps.add(e);
	}
	
	public List<T> removeQuantum() { //Devolve a lista de quantidade de instruções utilizadas
		List<T> q = quantum;
		quantum = new ArrayList<>();
		return q;
	}
	
	public List<T> removeSwap() { //Devolve a lista de quantidade de trocas
		List<T> s = swaps;
		swaps = new ArrayList<>();
		return s;
	}
}
