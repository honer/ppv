package MoviePlayer;

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
 * 而此主要就是跟據和每個代理伺服器的單位要求位元組,然後分配要求給每一個代裡伺服器
 * @author 廷鴻
 */
public class Play_Movies_Manerger_Thread extends Thread{

    @Override
    public void run()
    {

       try{
                int request_time = 0;
                int pre_startindex = 0;

            while(true)
            {
                for (int i = 0 ; i < Command.pre_sup_proxy_count; i++)
                {
                    if ( Command.Connect_Status_With_Proxy[i] == 1)
                        if (Command.stack_request_data_to_proxy_of_send[i].is_data1_full() == false)
                        {
                            Command.stack_request_data_to_proxy_of_send[i].push(pre_startindex,Command.request_length_to_proxy[i]);
                            request_time ++;
                            pre_startindex += Command.request_length_to_proxy[i];
                        }
                }

                Thread.sleep(100);
            }

        }catch(Exception e){
             //System.out.println("Play_Movies_Manerger_Thread");
             //System.out.println(e.toString());
    
        }


    }
}
