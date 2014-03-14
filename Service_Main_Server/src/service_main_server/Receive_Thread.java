package service_main_server;
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
public class Receive_Thread extends Thread{

    @Override
    public void run()
    {
        while(true)
        {

            try{

                byte[] receive = new byte[Command.Stack_len];
                int len;
           
                for(int i = 0 ; i < Command.Max_Client_Count ; i++)
                {
                    try{

                        if (Command.Connect_Status[i] == 1)                           
                        if (Command.Input_Stream[i].available() > 0)
                        {
                            //System.out.println("查看連線 " + i + " 有無資料");
                                len = Command.Input_Stream[i].read(receive);

                                if (len > 0)
                                    Command.Receive_Stack[i].push(receive, len);
                            }

                      }catch(Exception e){
                            Command.Connect_Status[i]  = 0;
                            Command.Work_Socket[i].shutdownInput();                            
                            //Command.Work._Socket[i].close();
                            System.out.println("Receive_Thread_Loop");
                            System.out.println(e.toString());
                      }
                }

                 Thread.sleep(100);
           

            }catch(Exception e){
                System.out.println("Receive_Thread");
                System.out.println(e.toString());
            }
        }
    }    

}
