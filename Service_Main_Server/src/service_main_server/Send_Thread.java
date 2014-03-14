/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_main_server;
import java.net.*;
import java.io.*;
/**
 *
 * @author 廷鴻
 */
public class Send_Thread extends Thread{

    @Override
    public void run()
    {
        while(true)
        {

            try{

                byte[] send = new byte[Command.Stack_len];
                int len;

            
                for(int i = 0 ; i < Command.Max_Client_Count; i++)
                {
                    try
                    {
                        
                           // if (!Command.Work_Socket[i].isOutputShutdown())
                            if (Command.Connect_Status[i] == 1)
                            {
                                len = Command.Send_Stack[i].pop_len();
                                send = Command.Send_Stack[i].pop();
            
                                if (len > 0)
                                    Command.Output_Stream[i].write(send, 0, len);
                            }

                    }catch(Exception e){
                        Command.Connect_Status[i]  = 0;
                        Command.Work_Socket[i].shutdownOutput();                        
                        //Command.Work_Socket[i].close();
                        System.out.println("Send_Thread_Loop");
                        System.out.println(e.toString());
                    }
                }
                Thread.sleep(100);

            }catch(Exception e){
                System.out.println("Send_Thread");
                System.out.println(e.toString());
            }
        }
    }
}
