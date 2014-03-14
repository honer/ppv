/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_secondly_server;
// 負責接收來自主伺服器資料的執行緒
/**
 *
 * @author 廷鴻
 */
public class Receive_From_Main_Thread  extends Thread {

    @Override
    public void run()
    {
        try{

            System.out.println("和主伺服器通訊的接收執行緒開始運行");

            byte[] recevie = new byte[Command.STACK_LEN_TO_MAIN_SERVER];
            int rec_len = 0;
            while(true)
            {
                if (Command.Connect_Status_With_Main == 1)
                    if (Command.Socket_To_Main_Input_Stream.available() > 0)
                    {
                        //System.out.println("發現主伺服器有傳來資料");
                        rec_len = Command.Socket_To_Main_Input_Stream.read(recevie);

                        if (rec_len > 0)
                        {
                            String ss = new String(recevie,0,rec_len);
                            if (ss.equalsIgnoreCase("#$ins_connect")) // 假如是測試是否再線的指令,則須馬上回傳指令
                            {
                                    String send = "#$ins_ok";
                                    //System.out.println("回傳: " + send + " 到主伺服器");
                                    Command.Send_Stack_In_Main.push(send.getBytes(), send.getBytes().length);
                            }
                            else   // 其他才放入堆疊給其他程序處理
                                Command.Receive_Stack_In_Main.push(recevie, rec_len);
                        }
                    }
                Thread.sleep(100);
            }

          }catch(Exception e){
               System.out.println("Receive_From_Main_Thread");
               System.out.println(e.toString());
         }

    }
}
