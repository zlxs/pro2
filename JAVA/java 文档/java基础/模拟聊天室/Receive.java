package a;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Receive implements Runnable{
	private DataInputStream din=null;
	private boolean isRunning=true;
	private String s=null;

	public Receive(Socket a)
	{
		try {
			 din=new DataInputStream(a.getInputStream());
			 
		} catch (IOException e) {
			e.printStackTrace();
			isRunning=false;
			try {
				din.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public synchronized void receive()
	{
		try {
			 s=din.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
			isRunning=false;
			try {
				din.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	
	}

	@Override
	public void run() {
		while(isRunning)
		{
			receive();
			System.out.println(s);
		}
	}
	
}
