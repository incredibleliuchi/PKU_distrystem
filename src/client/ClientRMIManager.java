package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientRMIManager {
	
	
	
	
	public boolean sendTCPQuest() {
		
		String src = "";
		StringBuilder backdata = new StringBuilder();
		
		try {
			Socket socket = new Socket("162.105.146.33", 27183);
			
			OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
			InputStreamReader in = new InputStreamReader(socket.getInputStream());
			BufferedReader br = new BufferedReader(in);
			
			out.write(src);
			out.flush();
			socket.shutdownOutput();
			
			String temp = null;
			while ( (temp = br.readLine()) != null) {
				backdata.append(temp);
			}
			
			socket.close();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return false;
	}

}
