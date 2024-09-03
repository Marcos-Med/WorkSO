package Process;

import java.util.HashSet;

public class TableProcess { // Tabela de processos
	private static TableProcess obj;
	private HashSet<BCP> table; //Tabela suporta processos distintos apenas
	
	private TableProcess() {
		table = new HashSet<>();
	}
	
	public static TableProcess getInstance() {//Design Patterns Siglenton
		if(obj == null) {
			obj = new TableProcess();
		}
		
		return obj;
	}
	
	public void add(BCP process) { //Adiciona processo na tabela
		table.add(process);
	}
	
	public void remove(BCP process) { // Mata o processo
		try{
			table.remove(process);
		}
		catch(Exception e){
			System.out.println("No there are element!");
		}
	}
	
}
