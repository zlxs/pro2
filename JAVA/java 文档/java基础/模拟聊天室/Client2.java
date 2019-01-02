package a;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client2 {
 public static void main(String[] args){
		try {
			Socket a=new Socket("localhost",8888);
			new Thread(new Send(a)).start();
			new Thread(new Receive(a)).start();	
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
 }

}