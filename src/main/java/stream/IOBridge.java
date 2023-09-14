package stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOBridge {

	public static void write(InputStream is, OutputStream os) throws IOException {
		byte[] buffer = new byte[1024];
    	int bytesRead;
    	while ((bytesRead = is.read(buffer)) != -1) {
    		os.write(buffer, 0, bytesRead);
    	}
	}
	
}
