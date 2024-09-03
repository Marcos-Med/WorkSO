package items;

public interface Write { //Interface para arquivos que serão alterados
	public abstract void write(String input); //escreve no arquivo
	public abstract void swapFile(String filename);//troca o arquivo destino
	public abstract void close(); //fecha arquivo
}
