package items;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;

public class LogFile implements Write{ //Arquivo que contém as decisões do escalonador em conjunto com as
										//Estatísticas
	
	private static Write obj;
	private PrintWriter writer;
	private String filename;
	
	private LogFile() {
		Reader reader = ReaderFileOS.getInstance();
		try{
			int quantum  = Integer.parseInt(reader.readFile("src/programas/quantum.txt").get(0));
			if(quantum >= 10) filename = "src/output/log" + quantum + ".txt";
			else filename = "src/output/log0" + quantum + ".txt";
			FileWriter filewriter = new FileWriter(filename, true);
			writer = new PrintWriter(filewriter);
		}
		catch(IOException e) {
			System.out.println("Error file");
		}
	}
	
	public static Write getInstance() { //Design Patterns Siglenton
		if(obj == null) {
			obj = new LogFile();
		}
		return obj;
	}
	
	public void close() { //Fecha arquivo
		writer.close();
	}
	
	
	public void write(String input){ //Escreve o input no arquivo
		writer.println(input);
	}
	
	public void swapFile(String filename) { //Troca o arquivo destino
		this.filename = filename;
		try {
			writer = new PrintWriter(this.filename);
		}
		catch(IOException e) {
			System.out.println("Error File!");
		}
	}
	
	
}
