package a;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
 private static List<SendandReceive>list=new ArrayList<SendandReceive>();
 private ServerSocket s=null;
 private Socket b1=null;
 
 public Server()
 {
	 try {
		this.s=new ServerSocket(8888);
	} catch (IOException e) {
		e.printStackTrace();
		try {
			s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	} 
 }
 
 public class SendandReceive implements Runnable
 {
  private DataInputStream a=null;
  private DataOutputStream b=null;
  private boolean isRunning=true;
  
  public SendandReceive(Socket a)
  {
	  try {
		this.a=new DataInputStream(a.getInputStream());
		this.b=new DataOutputStream(a.getOutputStream());
	} catch (IOException e) {
		e.printStackTrace();
		isRunning=false;
		try {
			a.close();
			b.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
  }
  
  public String receive()
  {
	  String s=null;
	  try {
		s=a.readUTF();
	} catch (IOException e) {
		e.printStackTrace();
		isRunning =false;
		list.remove(this);
		try {
			a.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	  return s;
  }
  
public void send(String s)
{
	try {
		b.writeUTF(s);
		 b.flush();
	} catch (IOException e) {
        isRunning=false;
        list.remove(this);
        try {
			b.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		e.printStackTrace();
	}
}
  
  public void sendAll()//发送给所以其他的客户端
  {
	 String s=receive();
	 if(s!=""&&s!=null)
		{
			for(SendandReceive temp:list)
			{
				if(temp==this)
					continue;
				temp.send("IP_"+b1.getInetAddress()+"_端口_"+b1.getPort()+" "+s);
			}
		}  
  }
  
	 
	 @Override
	 public void run() {
		 while(isRunning)
		 {
		  sendAll();
		 }
      }
 }
 
 public String client()
 {	
	 while(true)
     {
	  try {
		   b1=s.accept();
		   SendandReceive client=new SendandReceive(b1);
		   Server.list.add(client);
		   new Thread(client).start();
		} catch (IOException e) {
		e.printStackTrace();
		}
	 }
 }

 public void getId()
 {
	 new Server().client();
 }
 
 public static void main(String[] args){
	new Server().client();
	
	
 }
}


 

