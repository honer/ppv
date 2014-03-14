package MoviePlayer;
import java.net.*;
import java.io.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */

// 和主伺服器的初始連線

public class Init_Con_With_Main_Server  extends Thread {
 

    @Override
    public void run()
    {
        try
        {
                try{

                    SocketAddress RemoveAddress = new InetSocketAddress(Command.Main_Server_Ip,Command.Main_Server_Port);
               	    //System.out.println("正在和主伺服器" + Command.Main_Server_Ip + ":" + Command.Main_Server_Port + " 做連接中.....");
			        Command.Socket_To_Main .connect(RemoveAddress);
                }
                catch(Exception ex)
                {
                     InetAddress main_server = InetAddress.getByName(Command.Main_Server_Ip);
                     SocketAddress RemoveAddress = new InetSocketAddress(main_server.toString(),Command.Main_Server_Port);
                	 //System.out.println("正在和主伺服器" + Command.Main_Server_Ip + ":" + Command.Main_Server_Port + " 做連接中.....");
                     Command.Socket_To_Main .connect(RemoveAddress);
                }

			
				//System.out.println("和主伺服器連接成功!!");
                Command.Socket_To_Main_Input_Stream = Command.Socket_To_Main.getInputStream();
                //System.out.println("和主伺服器輸入資料流串接成功!!");
                Command.Socket_To_Main_Output_Stream = new DataOutputStream(Command.Socket_To_Main.getOutputStream());
                //System.out.println("和主伺服器輸出資料流串接成功!!");
                Command.Connect_Status_With_Main = 1;

          }catch(Exception e){
                //System.out.println("Init_Con_With_Main_Server");
                //System.out.println(e.toString());
                Command.Connect_Status_With_Main = 0;
         }



    }

}
