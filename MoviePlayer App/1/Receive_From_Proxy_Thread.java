package MoviePlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Receive_From_Proxy_Thread extends Thread {

    @Override
    public void run()
    {

         try{
            
            //System.out.println("和代理伺服器通訊的接收執行緒開始運行");

            byte[] receive = new byte[Command.STACK_LEN_OF_PROXY_AND_CLIENT];
            int len;

            while(true)
            {
                for(int i = 0 ; i < Command.MAX_CONNECT_COUNT_TO_PROXY ; i++)
                {                  
                        if (Command.Connect_Status_With_Proxy[i] == 1)
                        {
                           ////System.out.println("X:" + Command.Socket_To_Proxy_Input_Stream[i].available());
                           // //System.out.println("Y:" + Command.request_length[i]);
                            //if (Command.Socket_To_Proxy_Input_Stream[i].available() > 0)
                           // {
                             //   //System.out.println("發現到代理伺服器: " + i + " 有傳來資料");
                             //   len = Command.Socket_To_Proxy_Input_Stream[i].read(receive);

                            //    if (len > 0)
                            //    {
                                    // //System.out.println("如下");
                                   // String ss = new String(receive);
                                   // //System.out.println(ss);
                                    
                            //        Command.Receive_Stack_In_Proxy[i].push(receive, len);
                                   // int a = 0;
                            //    }
                          //  }
                        }
                }

                Thread.sleep(100);
            }

        }catch(Exception e){
             //System.out.println("Receive_From_Proxy_Thread");
             //System.out.println(e.toString());
             System.exit(0);
        }

    }

}
