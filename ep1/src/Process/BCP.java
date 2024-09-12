package Process;

import java.util.List;
import java.util.Objects;

public class BCP implements Comparable<BCP>{ //Bloco de Controle do Processo
	private List<String> textMemory; //Programa
	private StateProcess state; //Estado do processo
	private final int priority; //Prioridade do processo
	private int credit; // Créditos atual do processo
	private final int quantum; // quantum de execução
	private int time; // tempo atual
	private int registerX; // Registrador X
	private int registerY; // Registrado Y
	private String name; //Nome do programa
	private int ProgramCounter; // PC
	private int wait; //tempo de aguardo no estado bloqueado
	
	public BCP(List<String> textMemory, int priority, int quantum) {
		if(textMemory != null) name = textMemory.removeFirst();
		this.textMemory = textMemory;
		this.priority = priority;
		this.quantum = quantum;
		time = this.quantum;
		credit = this.priority;
		ProgramCounter = 0;
		registerX = 0;
		registerY = 0;
		state = StateProcess.READY;
		wait = 2;
	}
	
	@Override
	public int compareTo(BCP process) { //Critério de ordenação
		return Integer.compare(this.credit, process.credit());
	}
	
	public int getX() {
		return registerX;
	}
	
	public int getY() {
		return registerY;
	}
	
	public void setX(int x) {
		registerX = x;
	}
	
	public void setY(int y) {
		registerY = y;
	}
	
	public StateProcess getState() {
		return state;
	}
	
	public void setState(StateProcess state) {
		this.state = state;
	}
	
	public String getName() {
		return name;
	}
	
	public void restart() {
		credit = priority;
	}
	
	public void reduceCredit() { //Reduz os créditos
		if (credit > 0) credit--;
	}
	
	private void nextInstruction() { //Incrementa o Program Counter
		if(ProgramCounter < 21) ProgramCounter++;
	}
	
	public String getInstruction() { //Pega a próxima instrução
		String instruction = textMemory.get(ProgramCounter);
		nextInstruction();
		return instruction;
	}
	
	public void reduceTime() { //reduz o tempo de execução
		if(time > 0) time--;
	}
	
	public void restartTime() {
		time = quantum;
	}
	
	public int getTime() {
		return time;
	}
	
	public int getWait() {
		return wait;
	}
	
	public void removeWait() { //reduz o tempo de espera
		wait--;
	}
	
	public void restartWait() {
		wait = 2;
	}
	
	public int credit() { // retorna os creditos atuais
		return this.credit;
	}
	
	@Override
	public int hashCode() { // Localização na tabela hash
		return Objects.hash(name);
	}
	
	@Override
	public boolean equals(Object obj) { //Critério de igualdade
		return this == obj;
	}
}
