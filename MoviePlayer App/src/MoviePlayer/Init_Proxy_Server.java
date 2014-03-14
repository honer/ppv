/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MoviePlayer;
import java.net.*;
import java.io.*;
/*
/**
 *
 * @author 廷鴻
 */
public class Init_Proxy_Server extends Thread {

    @Override
    public void run()
    {
        try{

            while(true)
            {
                for(int i = 0 ; i < Command.MAXT_CLIENT_COUNT_OF_REQUEST ; i++)
                {
                    if ( Command.Work_Socket[i] == null)
                    {
                        Command.Work_Socket[i] = Command.Listen_Socket.accept();
                        //System.out.println("遠端客戶" + i + "已連接");

                        Command.Input_Stream[i] = Command.Work_Socket[i].getInputStream();
                        //System.out.println("和客戶端 " + i + " 輸入資料流串接成功!!");
                        Command.Output_Stream[i] = new DataOutputStream(Command.Work_Socket[i].getOutputStream());
                        //System.out.println("和客戶端 " + i + " 輸出資料流串接成功!!");

                        Command.Receive_Stack[i] = new Stack(Command.STACK_LEVEL_OF_PROXY_AND_CLIENT,Command.STACK_LEN_OF_PROXY_AND_CLIENT);
                        Command.Send_Stack[i] = new Stack(Command.STACK_LEVEL_OF_PROXY_AND_CLIENT,Command.STACK_LEN_OF_PROXY_AND_CLIENT);
                        Command.Connect_Status_With_Other[i] = 1; // 成功建立連線
                    }
                }
           }

        }catch(Exception e){
             //System.out.println("Init_Proxy_Server");
            //System.out.println(e.toString());
            System.exit(0);
        }
    }
}
