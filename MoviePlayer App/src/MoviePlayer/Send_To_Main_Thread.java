package MoviePlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */
public class Send_To_Main_Thread  extends Thread {

    @Override
    public void run()
    {
        try{

            //System.out.println("和主伺服器通訊的傳送執行緒開始運行");

            int send_len;
            byte[] send_buff = new byte[Command.STACK_LEN_TO_MAIN_SERVER];

            while(true)
            {
                ////System.out.println("send_thread");

                if (Command.Connect_Status_With_Main == 1)
                {
                    send_len = Command.Send_Stack_In_Main.pop_len();
                    send_buff = Command.Send_Stack_In_Main.pop();

                    if (send_len > 0)
                    {
                       // //System.out.println("傳送 " + send_len +  " 位元組 到主伺服器");
                        Command.Socket_To_Main_Output_Stream.write(send_buff, 0, send_len);
                    }
                }
                Thread.sleep(100);
                    
            }
         }catch(Exception e){
               //System.out.println("Send_To_Main_Thread");
               //System.out.println(e.toString());
         }
    }

}
