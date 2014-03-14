package service_secondly_server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 此程序可以說是作為代理伺服器用的,主要是提供其他客戶端的影片要求,然後提供的程序
 * @author 廷鴻
 */
public class Suport_Movies_Thread extends Thread {

    @Override
    public void run()
    {
     

         while(true)
         {
             try{

                for(int i = 0 ; i < Command.MAXT_CLIENT_COUNT_OF_REQUEST ; i++)
                {
                    if (Command.Connect_Status_With_Other[i] == 1) // 正常連線狀態
                    {
                        // System.out.println("測試客戶端 " + i + " 有無資料");
                        int len = Command.Receive_Stack[i].pop_len();
                        byte[] bte = Command.Receive_Stack[i].pop();

                        if (len > 0)
                        {
                            String rec = new String(bte,0,len);
                            System.out.println("接收到客戶端 " + i + " 的資料:" + rec);

                            String[] ins = rec.split("_");

                            // 穫的要求回傳電影資料的指令
                            // 指令的格式: getmovies_檔案名_啟始的位元索引_要求的位元組長度
                            if (ins[0].equalsIgnoreCase("getmovies"))
                            {
                                String file_name = ins[1];
                                int start_index = Integer.parseInt(ins[2]);
                                int length = Integer.parseInt(ins[3]);

                                //Command.pre_send_back_index = (start_index / Command.pre_play_movie_download_temp_length) + 1; // 目前的檔案讀取串接次數索引

                                byte[] send = File_Reader.get_bytes(Command.suport_path +  "\\" + file_name,start_index,length);

                                Command.Send_Stack[i].push(send, send.length);

                                //Command.UdpPacket_To_Other =  new DatagramPacket(send, send.length, Command.Work_Socket[i].getInetAddress(), Command.Work_Socket[i].getPort());

                                //String ss = new String(send);
                                //System.out.println("回傳以下資料");
                                //System.out.println(ss);
                                System.out.println("回傳 " + send.length + " 個位元組 到客戶端 " + i);

                            }
                        }
                    }
                }

               Thread.sleep(100);
           
           }catch(Exception e){
                System.out.println("Receive_From_Other");
                System.out.println(e.toString());
           }
        }
    }
}
