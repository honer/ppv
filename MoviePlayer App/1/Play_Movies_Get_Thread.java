package MoviePlayer;
import java.net.*;
import java.io.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * * 影片的分配發送給每個代理伺服器的法則為,對每個代理伺服器的要求字串裡
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
 *
 * Play_Movies_Manerger_Thread
 * Play_Movies_Get_Thread
 * Play_Movies_Request_Thread
 * 此三隻程序主要就是作為他其代裡伺服器的客戶端的程序,簡單來說就是影片的播放程序,
 * 而此主要就是將 Play_Movies_Request_Thread 要求給代裡伺服器,後來回傳
 * 來的資料,做組合播放之用
 * @author safe
 */
public class Play_Movies_Get_Thread extends Thread {

    @Override
    public void run()
    {
          
             int write_count = 0; // 檔案寫入的次數,可以做為接收完成的次數
             int finish_receive_byte_length = 0 ; // 目前完成接收的位元組長度,對於暫存器的空間來講
             int pr_rec_index = 0;  // 接收暫存器的存放索引
             int pre_write_count = 0 ;  // 目前影片接收後寫入檔案的次數
             //int pre_rec_stack_index = 0; // 堆疊的讀取索引
             int all_finish_receive_byte_length = 0; // 目前完成接收的位元組長度,對於整個檔案的長度來講

         

             write_count = Command.pre_play_movie_file_length / Command.pre_play_movie_download_temp.length ;  // write_count:為影片總共要寫入檔案的次數
             if (Command.pre_play_movie_file_length % Command.pre_play_movie_download_temp.length > 0)
                 write_count++;

             int debug = 0;
             String debug2 = "";

             while(true)
             {
             try{
                // 開始逐步接收代理伺服器回傳回來的資料

               for (int i = 0 ; i < Command.pre_sup_proxy_count; i++)
               {
                   int count = 0 ;

                    if (Command.Connect_Status_With_Proxy[i] == 1)
                    {
                    // rec_leghth = 0;

                    while(true)
                    {
                        count ++;

                        if(count == 30) // 已經發送3秒但尚未得到回應,所以強制不等待接收,可視為此代理伺服器已離線
                        {
                            Command.Connect_Status_With_Proxy[i] = 0;
                            Command.Socket_To_Proxy[i].shutdownInput();
                            Command.Socket_To_Proxy[i].shutdownOutput();
                            Command.Socket_To_Proxy[i].close();
                            //System.out.println("代理伺服器 " + i + " 已離線了");
                            break;         
                        }

                        if (Command.Socket_To_Proxy_Input_Stream[i].available() > 0 )
                        {                          
                            //System.out.println("發現到代理伺服器: " + i + " 有傳來資料");
                            
                            byte[] bte = new byte[Command.Socket_To_Proxy_Input_Stream[i].available()];                            
                            int len;
                            len = Command.Socket_To_Proxy_Input_Stream[i].read(bte);

                            // 讀取此封包要求的長度,的比對值
                            int check_length = Command.stack_request_data_to_proxy_of_check[i].pop_length();
                            // 讀取此封包要求的啟始索引,的比對值
                            int check_startindex = Command.stack_request_data_to_proxy_of_check[i].pop_startindex();

                             //System.out.println(i + " check_startindex: " + check_startindex);           

                            if (check_length != len) // 不正確應該重新要求
                            {                             

                                 //System.out.println("接收: " + len + " 個位元組 從代理伺服器: " + i + " 但是和要求的: " + check_length + " 不合,所以重新要求");

                                // 放入請求的堆疊,再一次請求此資料                              
                                 
                                 Command.wait_rec_flag[i]  = true; // 代表可以再放送下一個要求

                                 while (Command.stack_request_data_to_proxy_of_send[i].is_data1_full()) {}; // 如果滿了等有空間再放進去

                                 Command.stack_request_data_to_proxy_of_send[i].push(check_startindex,check_length);
                            }
                            else // 資料正確開始做資料重組的動作
                            {
                               // Command.add_v += 10;

                                Command.wait_rec_flag[i]  = true; // 代表可以再放送下一個要求                               

                                //System.out.println("接收: " + len + " 個位元組 從代理伺服器: " + i);
                                all_finish_receive_byte_length += len;
                                //System.out.println("目前總共接收: " + all_finish_receive_byte_length + " 個位元組 從全部的代理伺服器");

                                int ii = 0;
                                ////System.out.println("接收到 " + i + " 的以下資料");
                                //String ss = new String(bte);
                                ////System.out.println(ss);
                                
                                for(int j = check_startindex ; j < (check_startindex + len) ; j++ )
                                {
                                    pr_rec_index = j - pre_write_count * Command.pre_play_movie_download_temp_length;
                                    debug2 = "j:" + j + " pre_write_count:" + pre_write_count + " Command.pre_play_movie_download_temp_length:" + Command.pre_play_movie_download_temp_length;
                                    debug = pr_rec_index;
                                   
                                    // //System.out.println( i + " debug:" + debug);
                                    Command.pre_play_movie_download_temp[pr_rec_index] = bte[ii++];

                                    finish_receive_byte_length ++ ;
                                                                
                                    // 前面的等於:是暫存的空間已經滿了 , 後面的等於:是已經到達播放影片的長度了
                                    if (finish_receive_byte_length >= Command.pre_play_movie_download_temp_length
                                     || finish_receive_byte_length >= Command.pre_play_movie_file_length
                                     || finish_receive_byte_length >= (Command.pre_play_movie_file_length - Command.pre_play_movie_download_temp_length * pre_write_count))
                                    {
                                        // 將下載完成的資料存到檔案
                                       

                                        pre_write_count ++;

                                        if (pre_write_count == 1)// 建立新的檔案
                                            File_Writer.Write(Command.suport_path + "\\" + Command.pre_play_movie_file, Command.pre_play_movie_download_temp,finish_receive_byte_length,false);
                                        else                     // 重檔案的後面繼續寫入
                                            File_Writer.Write(Command.suport_path + "\\" + Command.pre_play_movie_file, Command.pre_play_movie_download_temp,finish_receive_byte_length,true);
                                        
                                        finish_receive_byte_length = 0;

                                        //System.out.println("寫入檔案,目前寫入第 " + pre_write_count + " 次");

                                        if (pre_write_count  == write_count) // 完整的寫入完畢
                                            return;
                                    }
                                }
                            }

                             break;
                           }

                          Thread.sleep(100);
                        }
                    }
               }
                 Thread.sleep(100);

                 }catch(Exception e){
                       //System.out.println("Play_Movies_Get_Thread");
                       //System.out.println(e.toString());
                       //System.out.println("debug:" + debug);
                       
                 }
             }
      
    }
 }
       

