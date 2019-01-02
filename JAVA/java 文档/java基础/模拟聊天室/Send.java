package a;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Send implements Runnable{
 private DataOutputStream a=null;
 private BufferedReader b=null;
 private boolean isRunning=true;
 private String s;
 private String name=null;
 
 public Send() 
 {
	 b=new BufferedReader(new InputStreamReader(System.in));
	 name="Client";
 }
 public Send(Socket a)
 {
	this();
	 try {
		this.a=new DataOutputStream(a.getOutputStream());
	} catch (IOException e) {
		e.printStackTrace();
		isRunning=false;
		try {
			b.close();
			a.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
 }
 
 public void setName(String name)
 {
	 this.name=name;
 }
 
 public synchronized void send()
 {
  try {
	s=b.readLine();
	if(!s.equals("")&&s!=null)
	a.writeUTF(name+" £º "+s);
	a.flush();
} catch (IOException e) {
	e.printStackTrace();
	isRunning=false;
	try {
		b.close();
		a.close();
	} catch (IOException e1) {
		e1.printStackTrace();
	}
}
 }
 
@Override
public void run() {
	while(isRunning)
	{
	 send();
	}
}
 
}
