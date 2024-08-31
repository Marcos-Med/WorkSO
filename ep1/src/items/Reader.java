package items;
import java.util.List;
import java.io.IOException;

public interface Reader {
	public abstract List<String> readFile(String file) throws IOException;
	public String getType();
}
