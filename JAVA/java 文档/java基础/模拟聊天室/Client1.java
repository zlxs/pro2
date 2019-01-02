package a;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client1 {
 public static void main(String[] args){
		try {
			Socket a=new Socket("localhost",8888);
			Send t=new Send(a);
			t.setName("Tom");//设置该客户端名称
			new Thread(t).start();
			new Thread(new Receive(a)).start();	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	 
 }

}
