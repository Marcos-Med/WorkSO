package items;

import java.io.PrintWriter;
import java.io.IOException;

public class LogFile implements Write{
	
	private static Write obj;
	private PrintWriter writer;
	private String filename;
	
	private LogFile() {
		filename = "../output/logfile.txt";
		try{
			writer = new PrintWriter(filename);
		}
		catch(IOException e) {
			System.out.println("Error file");
		}
	}
	
	public static Write getInstance() {
		return obj;
	}
	
	
	public void write(String input){
		writer.println(input);
	}
	
	public void swapFile(String filename) {
		this.filename = filename;
		try {
			writer = new PrintWriter(this.filename);
		}
		catch(IOException e) {
			System.out.println("Error File!");
		}
	}
	
	
}
