/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_secondly_server;
import java.net.*;
import java.io.*;
import java.net.*;
/**
 * 此執行緒是為了長住的處裡主伺服器和其他客戶端傳來的資料的
 * @author Administrator
 */
public class Manerger_Thread extends Thread{

    static int test_count = 0;

    @Override
    public void run()
    {
        try{

            Send_Pre_Suport_Files(); // 傳目前支援的檔案列表到主伺服器

            while(true)
            {
                Send_To_Main();  // 傳送資料到主伺服器
                Receive_From_Main(); // 接收主伺服器的資料
                Receive_From_Other(); // 自己為代理伺服器時接收其他用戶端的資料
               // Receive_From_Proxy(); // 接收來自代理伺服器的資料 // 此程序應該不會使用,因為會防害影片的接收

               // Test_Me_Be_A_Suport(); // 看自己是否成為一個新的支援者

                Thread.sleep(100);
            }

        }catch(Exception e){
             System.out.println("Manerger_Thread");
            System.out.println(e.toString());
          
        }
    }

    // 看自己是否成為一個新的支援者
    void Test_Me_Be_A_Suport()
    {
        test_count ++;

        if (test_count == 100) // 10秒判斷一次
        {
            test_count = 0;

            int file_size = 0;
        
            File s = new File(Command.suport_path + "\\" + Command.pre_play_movie_file);
        
            if (s.exists())
            {
               for (int i = 0 ; i < Command.list2.length ; i++)
               {           
                    String[] ss = Command.list2[i].split(" ");
                    if (ss[1].equalsIgnoreCase(Command.pre_play_movie_file))
                        file_size = Integer.parseInt(ss[2]);
               }
               
               if (s.length() >= (file_size / 5)) // 假如目前下載的長度超過原檔案的1/5則可以開始作為支援者
                    Send_Pre_Suport_Files(); // 傳目前支援的檔案列表到主伺服器
        
            }
        
        }
        
        

    }



    //傳送的資料格式為: 用戶端的編號_要傳送的資料


    void Receive_From_Proxy()
    {
            try{

            for(int i = 0 ; i < Command.MAX_CONNECT_COUNT_TO_PROXY ; i++)
            {
                if (Command.Connect_Status_With_Proxy[i] == 1) // 正常連線狀態
                {
                   // System.out.println("測試代理伺服器 " + i + " 有無資料");
                    int len = Command.Receive_Stack_In_Proxy[i].pop_len();
                    byte[] bte = Command.Receive_Stack_In_Proxy[i].pop();

                    if (len > 0)
                    {
                        String rec = new String(bte,0,len);
                        System.out.println("接收到代理伺服器" + i + " 的資料:" + rec);

                    }

                }
            }
        }catch(Exception e){
             System.out.println("Receive_From_Proxy");
            System.out.println(e.toString());

        }




    }

    void Receive_From_Main() // 接收主伺服器的資料
    {
       try{

           //if(Command.read_lock_flag)
          // {

                int len = Command.Receive_Stack_In_Main.pop_len();
                byte[] bte2 = Command.Receive_Stack_In_Main.pop();

                if (len > 0 )
                {
                    String ss = new String(bte2,0,len);
                    // System.out.println("接收到主伺服器傳來的資料: \n" + ss);                                      
                    System.out.println(ss);                    
                }
            //}

       }catch(Exception e){
             System.out.println("Receive_From_Main");
            System.out.println(e.toString());

        }



    }


    void Receive_From_Other() // 自己為代理伺服器時接收其他用戶端的資料
    {
       

    }


    void Send_Pre_Suport_Files() // 傳目前支援的檔案列表到主伺服器
    {
        try
        {
            File s = new File(Command.suport_path);
            String[] list = s.list();
            String ss = "";

            for(int i = 0 ; i < list.length ; i++)
                ss += list[i]+ "@";

           String myip = Share.get_real_ip();
            
          //  InetAddress[] myaddress = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            
          // for(int i = 0 ; i < myaddress.length ; i++)
          //      if (myaddress[i].toString().indexOf("192") < 0)
          //          myip = myaddress[i].toString();

            String send = "#$ins_suport_" + myip + "@" + Command.True_Use_Port + "_" + ss;
            System.out.println("###傳目前支援的檔案清單到主伺服器###");
            Command.Send_Stack_In_Main.push(send.getBytes(), send.getBytes().length);


        }catch(Exception e){

            System.out.println("Send_Pre_Suport_Files");
            System.out.println(e.toString());

        }

    }
   void Send_To_Main() // 傳送資料到主伺服器
    {
        try{

           if (Command.Keyin_To_Main.equalsIgnoreCase("") == false) // 有輸入資料要傳到主伺服器
           {
                String str = Command.Keyin_To_Main;

                if (str.indexOf("getlist1") >= 0)
                {
                  // Command.read_lock_flag = false; // 栓鎖起來防止被主讀取成程序欄截去
                    Get_List1(); // 讀取選單1資料
                  //  Command.read_lock_flag = true;
                }
                if (str.indexOf("getlist2") >= 0)
                {
                  //  Command.read_lock_flag = false; // 栓鎖起來防止被主讀取成程序欄截去
                    Get_List2(str.split("_")[1]); // 讀取選單2資料
                  //  Command.read_lock_flag = true;
                }
                if (str.indexOf("getmovies") >= 0)
                {
                  //  Command.read_lock_flag = false; // 栓鎖起來防止被主讀取成程序欄截去
                    int count = Get_Movies(str.split("_")[1]) ;
                    if(count > 0)
                    {
                        System.out.println("開始成功播放影片:提供的代理伺服器數量為:" + count);
                        System.out.println("要播放的影片大小為: " + Command.pre_play_movie_file_length);
                    }
                    else
                    {
                        System.out.println("播放影片失敗:因為提供的代理伺服器數量為:" + count);
                    }
                        //  Command.read_lock_flag = true;
                }
                if (str.equalsIgnoreCase("") == false)
                    Command.Send_Stack_In_Main.push(str.getBytes(), str.getBytes().length);

                Command.Keyin_To_Main = ""; // 清除
            }
       }catch(Exception e){
            System.out.println("Send_To_Main");
            System.out.println(e.toString());
            Command.Keyin_To_Main = "";
        }


    }
    int Get_Movies(String Movie_File)
    {
        try
        {
            for (int i = 0 ; i < Command.Connect_Status_With_Proxy.length ; i ++)
                Command.Connect_Status_With_Proxy[i] = 0; // 先清除為0

            String Send = "#$ins_suportget_" + Movie_File;
            Command.Send_Stack_In_Main.push(Send.getBytes(), Send.getBytes().length);
            System.out.println("發送要求電影:" + Movie_File + " 到主伺服器");

            Command.pre_play_movie_file = Movie_File; // 目前播放的影片檔名

            // 找出目前要播放的影片檔名的位元組長度
           // for(int i = 0 ; i < Command.list2.length ; i++)
           //     if (Command.list2[i].split(" ")[1].equalsIgnoreCase(Movie_File))
           //     {
           //         int file_len = Integer.parseInt(Command.list2[i].split(" ")[2]);
           //         Command.pre_play_movie_file_length = file_len;  // 目前背播放的影片的檔案位元組長度
                   // if (file_len > Command.pre_play_movie_download_temp_length )
                   //     Command.pre_play_movie_download_temp = new byte[Command.pre_play_movie_download_temp_length]; // 宣告目前要播放的影片暫存檔長度為3MBytes
                   // else
                   //     Command.pre_play_movie_download_temp = new byte[file_len]; // 宣告目前要播放的影片暫存檔長度為此檔案的位元組長度
           //     }




            int count = 0;
            int count2 = 0; //目前總共連接成功的代理伺服器數目
            String list = "";
            String size = "";

            while(true) // 等待接收此檔案的大小
            {
                Thread.sleep(100);
                count ++;

                if(count == 100) break; // 以經發送10秒但尚未得到回應,所以強制不等待接收列表

                int len = Command.Receive_Stack_In_Main.pop_len();
                byte[] bte2 = Command.Receive_Stack_In_Main.pop();

                if (len > 0 ) // 有接收到資料
                {
                    count = 0;

                    size = new String(bte2,0,len);

                    String[] ss = size.split("_");

                     if (ss[0].equalsIgnoreCase("ok"))
                     {                       
                        Command.pre_play_movie_file_length = Integer.parseInt(ss[1]);
                        System.out.println("要播放的影片大小為: " + Command.pre_play_movie_file_length);
                     }
                    break;
                }

            }

            count = 0;
            
            while(true) // 等待接收有支援此電影檔案的伺服器列表
            {
                Thread.sleep(100);
                count ++;

                if(count == 100) break; // 以經發送10秒但尚未得到回應,所以強制不等待接收列表

                int len = Command.Receive_Stack_In_Main.pop_len();
                byte[] bte2 = Command.Receive_Stack_In_Main.pop();

                if (len > 0 ) // 有接收到資料
                {
                     list = new String(bte2,0,len);

                     if (!list.equalsIgnoreCase(""))
                     {
                        String[] list2 = list.split("\n"); // 分析出每個列表

                        if (list2.length > 0)
                        {
                            for(int i = 0 ; i < list2.length ; i++)
                            {
                                if (list2[i].indexOf("@") >= 0)
                                {

                                    String ip = list2[i].split("@")[0];
                                    int port = Integer.parseInt(list2[i].split("@")[1]);

                                    if (ip.equalsIgnoreCase(Share.get_real_ip())) continue; // 假如IP是自己的跳過

                                    try{

                                        SocketAddress RemoveAddress = new InetSocketAddress(ip,port);
                                        System.out.println("正在和代理伺服器" + ip + ":" +port + " 做連接中.....");
                                        Command.Socket_To_Proxy[i] = new Socket();
                                        Command.Socket_To_Proxy[i].connect(RemoveAddress,3000);
                                        Command.Receive_Stack_In_Proxy[i] = new Stack(Command.STACK_LEVEL_OF_PROXY_AND_CLIENT,Command.STACK_LEN_OF_PROXY_AND_CLIENT);
                                        Command.Send_Stack_In_Proxy[i] =  new Stack(Command.STACK_LEVEL_OF_PROXY_AND_CLIENT,Command.STACK_LEN_OF_PROXY_AND_CLIENT);
                                        Command.Socket_To_Proxy_Input_Stream[i] =  Command.Socket_To_Proxy[i].getInputStream();
                                        Command.Socket_To_Proxy_Output_Stream[i] =  new DataOutputStream(Command.Socket_To_Proxy[i].getOutputStream());
                                        Command.Connect_Status_With_Proxy[i] = 1;
                                        // 對每個代理伺服器所要求的位元組長度和啟始索引堆疊
                                        Command.stack_request_data_to_proxy_of_send[i] = new Stack_Int_Min_Priority(Command.STACK_LEVEL_OF_PROXY_AND_CLIENT_2);
                                        // 對每個代理伺服器所要求的位元組長度和啟始索引的確認堆疊
                                        Command.stack_request_data_to_proxy_of_check[i] =  new Stack_Int(Command.STACK_LEVEL_OF_PROXY_AND_CLIENT_3);
                                        // 設定對每個代理伺服器的每次要求的資料長度,就是傳輸的速率                                      
                                        //
                                        Command.wait_rec_flag[i] = true;

                                        Command.IP_Of_Proxy[i] = ip;
                                        Command.Port_Of_Proxy[i] = port;

                                        count2++;

                                     }catch(Exception e){
                                        System.out.println("Get_Movies_2");
                                        System.out.println(e.toString());
                                     }

                                }
                            }
                        }
                    }

                     break;
                }
            }



            Command.pre_sup_proxy_count = count2; // 目前連接的代理伺服器數量

            if (count2 > 0) //假如有連接成功伺服器
            {
                Play_Movies_Request_Thread play_movie1 = new Play_Movies_Request_Thread();
                Play_Movies_Get_Thread play_movie2 = new Play_Movies_Get_Thread();
                Play_Movies_Manerger_Thread play_movie3 = new Play_Movies_Manerger_Thread();
                play_movie1.start();
                play_movie2.start();
                play_movie3.start();

                return count2;
            }
            else{
                return 0;
            }
         }catch(Exception e){
             System.out.println("Get_Movies");
             System.out.println(e.toString());
             return 0;
        }
    }

    void Get_List1() // 讀取選單1資料
    {
        try{

            String Send = "#$ins_get";
            Command.Send_Stack_In_Main.push(Send.getBytes(), Send.getBytes().length);
            System.out.println("發送要求選單指令到主伺服器");

            int count = 0;
            String list = "";

            while(true) // 等待接收選單資料
            {
                Thread.sleep(100);
                count ++;

                if(count == 100)
                {
                    System.out.println("接收餘時,未收到主伺服器的回應");
                    break; // 以經發送10秒但尚未得到回應,所以強制不等待接收列表
                }
                int len = Command.Receive_Stack_In_Main.pop_len();
                byte[] bte2 = Command.Receive_Stack_In_Main.pop();

                if (len > 0 )
                {
                     list = new String(bte2,0,len);
                     System.out.println("節目的選單1為:");
                     System.out.println(list);

                // 將選單1資料拆解後存放在陣列

                String[] ss = list.split("\n");
                int count2 = 0;

                for(int i = 0; i < ss.length; i++)
                    if(!ss[i].equalsIgnoreCase(""))
                        count2++;

                Command.list1 = new String[count2];

                for(int i = 0 ; i < count2 ; i++)
                    if(!ss[i].equalsIgnoreCase(""))
                        Command.list1[i] = ss[i];

                     break;
                }
            }




           }catch(Exception e){
                System.out.println("Get_List");
                System.out.println(e.toString());

        }
    }

    // list_num : 為節目由下往下的順續的編號,由0開始
    void Get_List2(String list_num) // 讀取選單2資料
    {
        try{

            String Send = "#$ins_list_" + list_num;
            Command.Send_Stack_In_Main.push(Send.getBytes(), Send.getBytes().length);
            System.out.println("發送要求選單2指令到主伺服器");

            int count = 0;
            String list = "";

            while(true) // 等待接收選單資料
            {
                Thread.sleep(100);
                count ++;

                if(count == 100) break; // 以經發送10秒但尚未得到回應,所以強制不等待接收列表

                int len = Command.Receive_Stack_In_Main.pop_len();
                byte[] bte2 = Command.Receive_Stack_In_Main.pop();

                if (len > 0 )
                {
                     list = new String(bte2,0,len);
                     break;
                }
            }

                System.out.println("節目的選單2為:");
                System.out.println(list);

                     // 將選單2資料拆解後存放在陣列

                String[] ss = list.split("\n");
                int count2 = 0;

                for(int i = 0; i < ss.length; i++)
                    if(ss[i].split(" ").length >= 3)
                        count2++;

                Command.list2 = new String[count2];

                for(int i = 0 ; i < count2 ; i++)
                     if(ss[i].split(" ").length >= 3)
                        Command.list2[i] = ss[i];

           }catch(Exception e){
                System.out.println("Get_List");
                System.out.println(e.toString());

        }
    }
}
