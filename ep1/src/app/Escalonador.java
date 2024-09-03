package app;
import machine.Machine;
import statics.Statiscs;
import items.Write;
import items.LogFile;

public class Escalonador {
	public static void main(String[] args) {
		System.out.println("Schedule");
		Machine machine = Machine.getInstance();
		int nProcess = 10;
		for(int i = 1; i <= nProcess; i++) {
			String name = "src/programas/";
			if(i >= 10) {
				name += (i + ".txt");
			}
			else {
				name += ("0" + i + ".txt");
			}
			machine.loadProcess(name, i);
		}
		
		machine.printLoading();
		
		while(machine.areThereProcess()) {
			machine.execute();
			machine.verifyBlocked();
			if(machine.isZeroPriority()) machine.creditRedistribution();
		}
		
		Statiscs math = Statiscs.getInstance();
		Write file = LogFile.getInstance();
		double averageQuantum = (double) math.sum(machine.returnQuantum())/nProcess;
		double averageSwaps = (double) math.sum(machine.returnSwaps())/nProcess;
		file.write("MEDIA DE TROCAS: " + averageQuantum);
		file.write("MEDIA DE INSTRUCOES: " + averageSwaps);
		file.write("QUANTUM: " + machine.getQuantum());
		machine.exit();
		System.out.println("End");
		}
}
