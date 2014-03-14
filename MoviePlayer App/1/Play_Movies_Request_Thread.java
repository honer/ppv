/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MoviePlayer;
import java.net.*;
import java.io.*;
/**
 * 影片的分配發送給每個代理伺服器的法則為,對每個代理伺服器的要求字串裡
 * 有一個此影片檔案的再全檔案裡面的一個跳位啟始索引,然後再一個長度
 * 然後此跳位啟始索引,和這一個長度,放每發一次要求就同步的放入一個check
 * 堆疊裡面,然後接收到資料後,會順續的從check堆疊裡面拿出資料,然後根據
 * 此拿出的跳位啟始索引,得知此接收到的資料是屬於整個檔案的那個部份的
 *
 * 而要求和接收之間還會有一個驗證的動作,如果接收到的資料和check拿出的
 * 當初要求長度不一樣,就當作傳輸錯誤,則此時會將此筆資料丟棄,而重新要求
 * 一次,但可能再要求時會因為之前已有很多的要求再排隊要等著要求,而導至
 * 此筆資料會來不及的得到,所以再要求的堆疊裡面使用Stack_Int_Min_Priority
 * 來作為要求堆疊物件,此物件有個特性就是,會做自動的排序作用,不像Stack_Int
 * 有一個先進先出的特性,而是會將跳位啟始索引由小自大排序,所以最小的跳位啟始
 * 索引會被優先要求
 *
 * Play_Movies_Manerger_Thread
 * Play_Movies_Get_Thread
 * Play_Movies_Request_Thread
 * 此三隻程序主要就是作為他其代裡伺服器的客戶端的程序,簡單來說就是影片的播放程序,
 * 而此主要就是將 Play_Movies_Manerger_Thread ,分配完的資料,足一的發送給每個代
 * 理伺服器,並將放送的要求,放1check堆疊裡面,然後Play_Movies_Get_Thread會跟據
 * check堆疊的資料,來將從代理伺服器接收的資料作組合的動作
 * @author Administrator
 */
public class Play_Movies_Request_Thread extends Thread {

    @Override
    public void run()
    {
             int request_time = 0 ; // 要求的次數
             int pr_rec_index = 0;  // 接收暫存器的存放索引
             int pre_rec_stack_index = 0; // 堆疊的讀取索引
             int request_length = 0;
             int request_startindex = 0;
          


         

        while(true)
        {
            try{
                // 先把每一個代理伺服器都要求一遍之後
                                
                int pre_proxy_count = 0 ; // 目前要求的代理伺服器的編號
             
           
                    for (int i = 0 ; i < Command.pre_sup_proxy_count; i++)
                    {
                        if (Command.Connect_Status_With_Proxy[i] == 1)
                            if (Command.wait_rec_flag[i])
                            {
                                if (Command.stack_request_data_to_proxy_of_check[i].is_data1_full() == false &&  Command.Send_Stack_In_Proxy[i].is_data1_full() == false) // 要看check的堆疊有無滿
                                {
                            
                                    request_time ++;

                                    //System.out.println("要求給代理伺服器第 " +request_time + " 次");
                                    ////System.out.println(i + " send_point1:" + Command.stack_request_data_to_proxy_of_send[i].pre_point1());
                                    request_length = Command.stack_request_data_to_proxy_of_send[i].pop_length();
                                    request_startindex  = Command.stack_request_data_to_proxy_of_send[i].pop_startindex();
                                    ////System.out.println(i + " send_point1:" + Command.stack_request_data_to_proxy_of_send[i].pre_point1());

                                    if (request_length > 0)
                                    {
                                        Command.stack_request_data_to_proxy_of_check[i].push(request_startindex,request_length);
                                        //System.out.println("放入 " + i + " check: request_startindex " + request_startindex + " request_length:" + request_length);

                                        String send = "getmovies_" + Command.pre_play_movie_file + "_" + request_startindex +  "_" + request_length;
                                        Command.Send_Stack_In_Proxy[i].push(send.getBytes(), send.getBytes().length);
                                        //System.out.println("傳送字串: " + send + " 到代理伺服器: " + i);

                                        // 把要求的資料放入堆疊,做後check之用
                                  
                                        Command.wait_rec_flag[i]  = false; // 等待要求有回應之後,再把此值high起來,才再發送下一個要求
                                     }
                                }
                            }
                    }
                
                Thread.sleep(100);

            }catch(Exception e){
                //System.out.println("Play_Movies_Request_Thread");
                //System.out.println(e.toString());
            }
        }      
    }
}


