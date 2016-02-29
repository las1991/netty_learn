package bio;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TimeClient {
	public static void main(String[] args) throws InterruptedException {
		int port = 15454;
		if (args != null) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}
		Socket socket = null;
//		BufferedReader in = null;
		PrintWriter out = null;
		
		try {
			socket = new Socket("localhost", port);
//			in = new BufferedReader(new InputStreamReader(
//					socket.getInputStream()));
			while(true){
				Thread.sleep(1000);
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println("");
			out.flush();
			out.println("{   \"action\" : \"heartbeat\",   \"cur_conn\" : 18,   \"max_conn\" : 100,   \"media_status\" : 200,   \"resource_addr\" : \"127.0.0.1\",   \"resource_port\" : \"1234\",   \"resource_type\" : \"storage\",   \"type\" : 1}");
			out.flush();
			out.println("#");
			out.flush();
			System.out.println("Send order 2 server success!	");
			}
//			String resp = in.readLine();
//			System.out.println("Now is :" + resp);
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
//			if(in!=null){
//				try {
//					in.close();
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//			}
			if(out!=null){
				out.close();
				out=null;
			}
			
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket=null;
			}
		}
	}
}
