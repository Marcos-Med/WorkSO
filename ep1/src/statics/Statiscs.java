package statics;

import java.util.List;

public class Statiscs {
	private static Statiscs obj;
	private String type;
	
	private Statiscs() {
		type = "math";
	}
	
	public static Statiscs getInstance() {
		if(obj == null) {
			obj = new Statiscs();
		}
		
		return obj;
	}
	
	public String getType() {
		return type;
	}
	
	public double sum(List<Integer> numbers) {
		double a = 0;
		for(int e: numbers) {
			a += e;
		}
		
		return a;
	}
}
