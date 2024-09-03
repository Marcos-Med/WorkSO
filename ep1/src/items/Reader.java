package items;
import java.util.List;
import java.io.IOException;

public interface Reader { //Interface dos arquivos que serão lidos
	public abstract List<String> readFile(String file) throws IOException;
	public String getType();
}
