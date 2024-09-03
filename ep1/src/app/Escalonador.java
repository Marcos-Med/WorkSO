package app;
import machine.Machine;
import statics.Statiscs;
import items.Write;
import items.LogFile;

public class Escalonador {
	//Classe principal que realiza o escalonamento dos processos
	public static void main(String[] args) {
		System.out.println("Schedule");
		Machine machine = Machine.getInstance();
		int nProcess = 10;
		for(int i = 1; i <= nProcess; i++) { //Carrega os programas  na memória
			String name = "src/programas/";
			if(i >= 10) {
				name += (i + ".txt");
			}
			else {
				name += ("0" + i + ".txt");
			}
			machine.loadProcess(name, i);
		}
		
		machine.printLoading(); //Imprime o estado da fila de prontos
		
		while(machine.areThereProcess()) { //Ciclo de execução
			machine.execute();
			if(machine.isZeroPriority()) machine.creditRedistribution(); //Redistribuição dos créditos
		}
		
		Statiscs math = Statiscs.getInstance();
		Write file = LogFile.getInstance();
		double averageQuantum = (double) math.sum(machine.returnQuantum())/nProcess;
		double averageSwaps = (double) math.sum(machine.returnSwaps())/nProcess;
		file.write("MEDIA DE TROCAS: " + averageSwaps);
		file.write("MEDIA DE INSTRUCOES: " + averageQuantum);
		file.write("QUANTUM: " + machine.getQuantum());
		machine.exit();
		System.out.println("End");
		}
}
