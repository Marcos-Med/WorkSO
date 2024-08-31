package items;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class ReaderFileOS implements Reader {
	private static ReaderFileOS obj;
	private String type;
	
	private ReaderFileOS() {
		type = "FileOS";
	}
	
	public static Reader getInstance() {
		if(obj == null) {
			obj = new ReaderFileOS();
		}
		return obj;
	}
	
	public String getType() {
		return type;
	}
	
	public List<String> readFile(String name) throws IOException{
		List<String> listFiles = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(name));
		String line;
		while((line = br.readLine()) != null) {
			listFiles.add(line);
		}
		br.close();
		return listFiles;
	}
}
