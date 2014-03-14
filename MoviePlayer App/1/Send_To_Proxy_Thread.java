package MoviePlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
public class Send_To_Proxy_Thread extends Thread {

    @Override
    public void run()
    {
         try{

            //System.out.println("和代理伺服器通訊的傳送執行緒開始運行");

            byte[] send = new byte[Command.STACK_LEN_OF_PROXY_AND_CLIENT];
            int len;

            while(true)
            {
                for(int i = 0 ; i < Command.MAX_CONNECT_COUNT_TO_PROXY; i++)
                {
                        if (Command.Connect_Status_With_Proxy[i] == 1)
                        {
                            len = Command.Send_Stack_In_Proxy[i].pop_len();
                            send = Command.Send_Stack_In_Proxy[i].pop();

                            if (len > 0)
                            {
                                //System.out.println("Thread:傳送資料到代理伺服器 " + i);
                                Command.Socket_To_Proxy_Output_Stream[i].write(send, 0, len);
                            }
                        }
                }

                Thread.sleep(100);
            }

        }catch(Exception e){
            //System.out.println("Send_To_Proxy_Thread");
            //System.out.println(e.toString());
        }
    }
}
