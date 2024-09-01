package app;
import machine.Machine;

public class Escalonador {
	public static void main(String[] args) {
		System.out.println("Schedule");
		Machine machine = Machine.getInstance();
		for(int i = 1; i < 11; i++) {
			String name = "../programas/";
			if(i >= 10) {
				name.concat(i + ".txt");
			}
			else {
				name.concat("0" + i + ".txt");
			}
			machine.loadProcess(name, i);
		}
		
		while(machine.areThereProcess()) {
			boolean flag = machine.execute();
			if(flag) machine.verifyBlocked();
		}
	}
}
