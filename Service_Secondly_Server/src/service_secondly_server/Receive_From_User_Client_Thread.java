/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_secondly_server;

/**
 *
 * @author 廷鴻
 */
public class Receive_From_User_Client_Thread extends Thread {

    @Override
    public void run()
    {
        try{

            System.out.println("和其他使用者通訊的接收執行緒開始運行");

            byte[] receive = new byte[Command.STACK_LEN_OF_PROXY_AND_CLIENT];
            int len;

            while(true)
            {
                for(int i = 0 ; i < Command.MAXT_CLIENT_COUNT_OF_REQUEST ; i++)
                {      
                        if (Command.Connect_Status_With_Other[i] == 1)
                        {                           
                            if (Command.Input_Stream[i].available() > 0)
                            {
                                System.out.println("發現用戶端: " + i + " 有傳來資料");
                                len = Command.Input_Stream[i].read(receive);

                                if (len > 0)
                                    Command.Receive_Stack[i].push(receive, len);
                            }
                        }
                }

                Thread.sleep(100);
            }

        }catch(Exception e){
            System.out.println("Receive_From_User_Client_Thread");
            System.out.println(e.toString());          
        }
    }
}
