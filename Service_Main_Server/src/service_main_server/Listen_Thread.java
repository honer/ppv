package service_main_server;
import java.net.*;
import java.io.*;
/**
 *
 * @author 廷鴻
 */
public class Listen_Thread extends Thread {

    @Override
    public void run()
    {
        int ii = 0;        
        try{


            while(true)
            {            
                for(int i = 0 ; i < Command.Max_Client_Count; i++)
                {                  
                    boolean is_no_use = false;

                    if (Command.Connect_Status[i] == 0)
                        is_no_use = true;

                    if (is_no_use)
                    {
                        Command.Work_Socket[i] = Command.Listen_Socket.accept();
                        System.out.println("遠端客戶 " + i + " 已連接 IP:" + Command.Work_Socket[i].getInetAddress().toString());

                        Command.Input_Stream[i] = Command.Work_Socket[i].getInputStream();
                        System.out.println("和客戶端 " + i + " 輸入資料流串接成功!!");
                        Command.Output_Stream[i] = new DataOutputStream(Command.Work_Socket[i].getOutputStream());
                        System.out.println("和客戶端 " + i + " 輸出資料流串接成功!!");

                        Command.Receive_Stack[i] = new Stack(Command.Stack_Level,Command.Stack_len);
                        Command.Send_Stack[i] = new Stack(Command.Stack_Level,Command.Stack_len);
                        Command.Connect_Status[i] = 1; // 成功建立連線
                        break;
                    }
                }

                Thread.sleep(100);
           }

        }catch(Exception e){
             System.out.println("Listen_Thread");
            System.out.println(e.toString());
            System.exit(0);
        }

        

    }


}
