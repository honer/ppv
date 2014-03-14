/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_secondly_server;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.applet.*;
/**
 *
 * @author 廷鴻
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       try{
            init();

            //*** 作為一般伺服器和主伺服器通訊

            Init_Con_With_Main_Server Thread1 = new Init_Con_With_Main_Server();
            Thread1.start();

            while (true)
            {
                if (Command.Connect_Status_With_Main == 1) // 已建立連線
                {
                    Receive_From_Main_Thread Thread2 = new Receive_From_Main_Thread();
                    Thread2.start(); // 接收來自主伺服器資料的執行緒
                    Send_To_Main_Thread Thread3 = new Send_To_Main_Thread();                  
                    Thread3.start(); // 要發送資料到主伺服器的執行緒
                    break;
                }
            }

            //***

            //*** 作為代理伺服器和一般的使用者通訊

            Init_Proxy_Server Thread5 = new Init_Proxy_Server(); // 初始化自己為其他使用者下載影片的代理伺服器連線
            Thread5.start();

            Receive_From_User_Client_Thread Thread6 = new Receive_From_User_Client_Thread(); // 接收其他使用者傳來的資料的執行緒
            Thread6.start();

            Send_To_User_Client_Thread Thread7 = new Send_To_User_Client_Thread(); // 傳送資料到其他使用者的執行緒
            Thread7.start();

            //***

            //*** 作為一般的用戶端和代理伺服器作通訊

            Receive_From_Proxy_Thread Thread8 = new Receive_From_Proxy_Thread();
            Send_To_Proxy_Thread Thread9 = new Send_To_Proxy_Thread();
            Thread8.start();
            Thread9.start();

            //*** 主管理的
            //此執行緒是為了長住的處裡和主伺服器來往的資料的程序
            Manerger_Thread Thread4 = new Manerger_Thread();
            Thread4.start();
            
            //此執行緒是為了接收外端輸入控制命令到主伺服器的
            Keyin_Manerger_Thread Thread10 = new Keyin_Manerger_Thread(); //
            Thread10.start();


            // 此為作為代理伺服器提供其他客戶端求影片的提供程序
            Suport_Movies_Thread Thread11 = new Suport_Movies_Thread();
            Thread11.start();


            }catch(Exception e){
                System.out.println("main");
                System.out.println(e.toString());
                Command.Connect_Status_With_Main = 0;
            }

    }

      public static void init()
    {
      try{
             // 連線到主伺服器           
             Command.Receive_Stack_In_Main = new Stack(Command.STACK_LEVEL_TO_MAIN_SERVER,Command.STACK_LEN_TO_MAIN_SERVER);
             Command.Send_Stack_In_Main = new Stack(Command.STACK_LEVEL_TO_MAIN_SERVER,Command.STACK_LEN_TO_MAIN_SERVER);
             Command.Socket_To_Main = new Socket();


             // 當其它代理伺服器的客戶端
             Command.Proxy_Server_Ip = new String[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Socket_To_Proxy = new Socket[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Proxy_Server_Port = new int[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Socket_To_Proxy_Input_Stream = new InputStream[Command.MAX_CONNECT_COUNT_TO_PROXY] ;
             Command.Socket_To_Proxy_Output_Stream = new DataOutputStream[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Connect_Status_With_Proxy = new int[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Receive_Stack_In_Proxy = new Stack[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Send_Stack_In_Proxy = new Stack[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.UdpSocket_To_Porxy = new DatagramSocket();
             Command.pre_play_movie_download_temp = new byte[Command.pre_play_movie_download_temp_length];
             Command.stack_request_data_to_proxy_of_send = new Stack_Int_Min_Priority[Command.MAX_CONNECT_COUNT_TO_PROXY];
             //Command.stack_request_startindex_to_proxy_of_send = new Stack_Int_Min_Priority[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.stack_request_data_to_proxy_of_check = new Stack_Int[Command.MAX_CONNECT_COUNT_TO_PROXY];
             //Command.stack_request_startindex_to_proxy_of_check = new Stack_Int[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.Port_Of_Proxy = new int[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.IP_Of_Proxy = new String[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.request_length_to_proxy = new int[Command.MAX_CONNECT_COUNT_TO_PROXY];
             Command.wait_rec_flag = new boolean[Command.MAX_CONNECT_COUNT_TO_PROXY];
             //
             for (int i = 0 ; i < Command.MAX_CONNECT_COUNT_TO_PROXY ; i++)
                Command.request_length_to_proxy[i] = Command.DEFAULT_REQUEST_LENGTH;

             // 作為代理伺服器時
             Command.True_Use_Port = Command.Proxy_Default_Port;
             Command.Input_Stream = new InputStream[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Output_Stream = new DataOutputStream[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Connect_Status_With_Other= new int[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Work_Socket = new Socket[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Send_Stack = new Stack[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Receive_Stack = new Stack[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.UdpSocket_To_Other = new DatagramSocket();

           

             while (true)
             {
                try{
                    if (Command.True_Use_Port > 10000) break;
                    Command.Listen_Socket = new ServerSocket(Command.True_Use_Port);
                    System.out.println("建立代理伺服器端在Port:" + Command.True_Use_Port);
                    break;
                }
                catch(Exception ex)
                {
                   Command.True_Use_Port ++;
                }
             }

             

             Command.Work_Socket = new Socket[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Connect_Status = new int[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Receive_Stack = new Stack[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Send_Stack = new Stack[Command.MAXT_CLIENT_COUNT_OF_REQUEST];
             Command.Input_Stream = new InputStream[Command.MAXT_CLIENT_COUNT_OF_REQUEST] ;
             Command.Output_Stream = new DataOutputStream[Command.MAXT_CLIENT_COUNT_OF_REQUEST];

        }catch(Exception e){
               System.out.println("init");
            System.out.print(e.toString());
           
        }

    }

   

}
