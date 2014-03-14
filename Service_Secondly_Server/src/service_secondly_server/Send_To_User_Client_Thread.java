/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_secondly_server;

/**
 *
 * @author 廷鴻
 */
public class Send_To_User_Client_Thread extends Thread {

    @Override
    public void run()
    {
        try{
            
            System.out.println("和其他使用者通訊的傳送執行緒開始運行");

            byte[] send = new byte[Command.STACK_LEN_OF_PROXY_AND_CLIENT];
            int len;

            while(true)
            {
                for(int i = 0 ; i < Command.MAXT_CLIENT_COUNT_OF_REQUEST; i++)
                {        
                        if (Command.Connect_Status_With_Other[i] == 1)
                        {
                         
                           len = Command.Send_Stack[i].pop_len();
                           send = Command.Send_Stack[i].pop();

                        //   byte[] bb = new byte[1];
                        //   bb[0] = 10;

                           if (len > 0)
                           {
                                System.out.println("Thread:傳送資料到用戶端: " + i );
                                Command.Output_Stream[i].write(send, 0, len);
                           }
                        }
                }

                Thread.sleep(100);
            }

        }catch(Exception e){
            System.out.println("Send_To_User_Client_Thread");
            System.out.println(e.toString());          
        }
    }
}
