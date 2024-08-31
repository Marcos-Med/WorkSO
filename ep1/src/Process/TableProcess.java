package Process;

import java.util.HashSet;

public class TableProcess {
	private static TableProcess obj;
	private HashSet<BCP> table;
	
	private TableProcess() {
		table = new HashSet<>();
	}
	
	public static TableProcess getInstance() {
		if(obj == null) {
			obj = new TableProcess();
		}
		
		return obj;
	}
	
	public void add(BCP process) {
		table.add(process);
	}
	
	public void remove(BCP process) {
		try{
			table.remove(process);
		}
		catch(Exception e){
			System.out.println("No there are element!");
		}
	}
	
}
