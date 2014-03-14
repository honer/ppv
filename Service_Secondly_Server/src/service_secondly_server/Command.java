package service_secondly_server;
import java.net.*;
import java.io.*;
/**
 *
 * @author 廷鴻
 */
public class Command {
    
   // 當主伺服器的客戶端 
   public static String Main_Server_Ip = "www.uplife.com.tw";
   public static int Main_Server_Port = 3388;
   public static Socket Socket_To_Main = null;
   public static InputStream Socket_To_Main_Input_Stream = null;
   public static DataOutputStream Socket_To_Main_Output_Stream = null;
   public static int STACK_LEVEL_TO_MAIN_SERVER = 1024; // 和主伺服器通訊的堆碟深度
   public static int STACK_LEN_TO_MAIN_SERVER = 1024; // 和主伺服器通訊的位元陣列的長度
   public static Stack Receive_Stack_In_Main = null;
   public static Stack Send_Stack_In_Main = null;
   public static int Connect_Status_With_Main; // 和主伺服器的連線狀態
 //  public static boolean read_lock_flag = false; // 此為資料讀取的栓鎖旗標,為了防止讀取的資料被其他的程序讀去
   public static String Keyin_To_Main = "" ; // 外界的輸入資料要傳到主伺服器的


   // 當其它代理伺服器的客戶端
   public static int[] Proxy_Server_Port;
   public static String[] Proxy_Server_Ip;
   public static int MAX_CONNECT_COUNT_TO_PROXY = 100; // 允許連接的最大的代理伺服器數量
   public static Socket[] Socket_To_Proxy = null;
   public static InputStream[] Socket_To_Proxy_Input_Stream = null;
   public static DataOutputStream[] Socket_To_Proxy_Output_Stream = null;
   public static Stack[] Receive_Stack_In_Proxy;
   public static Stack[] Send_Stack_In_Proxy;
   public static int[] Connect_Status_With_Proxy; // 和代理伺服器的連線狀態
   public static String[] list1; // 存放選單1的資料
   public static String[] list2; // 存放選單2的資料\
   public static int pre_sup_proxy_count = 0; // 目前提供影片的代理伺服器數量
   public static String pre_play_movie_file = null; // 目前正在播放的影片的檔名
   public static byte[] pre_play_movie_download_temp ; // 目前播放的影片下載中的暫存位置
   public static int pre_play_movie_download_temp_length = 1048576 ; // 目前播放的影片下載中的暫存位置的記億體位置長度
   public static int pre_play_movie_file_length ; // 目前背播放的影片的檔案位元組長度
   //public static int pre_send_back_index; // 檔案目前回傳的索引,用以判斷讀檔的長度位置索引;
   public static int finish_rec_file_Length = 0; // 目前已經回傳完畢的檔案位元組長度
   public static DatagramSocket UdpSocket_To_Porxy;
   public static DatagramPacket UdpPacket_To_Proxy;
   public static String[] IP_Of_Proxy; // 每個代理伺服器的IP位址
   public static int[] Port_Of_Proxy; // 每個代理伺服器的Port
    public static Stack_Int_Min_Priority[] stack_request_data_to_proxy_of_send; // 對每個代理伺服器所要求的長度,此為發送的命令
    //public static Stack_Int_Min_Priority[] stack_request_startindex_to_proxy_of_send; // 對每個代理伺服器所要求的檔案啟始索引,此為發送的命令
    public static Stack_Int[] stack_request_data_to_proxy_of_check; // 對每個代理伺服器所要求的長度,此為發送後存在此裡面最比對用的,不對的話要求重新發送
    //public static Stack_Int[] stack_request_startindex_to_proxy_of_check; // 對每個代理伺服器所要求的檔案啟始索引,此為發送後存在此裡面最比對用的,不對的話要求重新發送
    public static int STACK_LEVEL_OF_PROXY_AND_CLIENT_2 = 30;  // 存放用戶端請求代理伺服器回傳的長度和啟始索引的堆疊最大深度
    //public static int STACK_LEN_OF_PROXY_AND_CLIENT_2 = 1024; // 存放用戶端請求代理伺服器回傳的啟始索引和長度(和確認的啟始索引和長度)的堆疊最大深度
    public static int STACK_LEVEL_OF_PROXY_AND_CLIENT_3 = 30;// 存放用戶端請求代理伺服器回傳的確認長度和啟始索引的堆疊最大深度
    //public static int STACK_LEN_OF_PROXY_AND_CLIENT_3= 1024; // 存放用戶端請求代理伺服器回傳的長度的索引長度的最大值
    public static int MAX_REQUEST_LENGTH_TO_PRXOXY = 10240; // 最大的用戶端請求代理伺服器回傳的長度
    public static int[] request_length_to_proxy; // 對每個代理司服器的每次要求的單位資料長度, << 此值決定和每個代理伺服器的傳輸的速率 >>
    public static int DEFAULT_REQUEST_LENGTH = 5000;// 對每個代理司服器的每次要求的預設單位資料長度<< 此值決定和每個代理伺服器的傳輸的速率 >> << 和上面的值做對應,會跟據每個代理伺服器的網路品值來做增減的微調 >>
    public static boolean[] wait_rec_flag; // 等待接收的旗標,hight起來帶表此次的要求已接收到回應;

    public static int P = 1;
    public static int I = 1;
    public static int D = 1;
    
    public static int I_add = 0;
    public static int D_old = 0;

    public static int add_v = 0;
    //public static int del_v = 0;



    // 作為代理伺服器時
    public static ServerSocket Listen_Socket; //
    public static Socket[] Work_Socket;
    public static int MAXT_CLIENT_COUNT_OF_REQUEST = 10000; // 允許連接的數量
    public static int Proxy_Default_Port = 3366; //遇設的連接port,建立失敗時會以此值開始累加
    public static int True_Use_Port;
    public static InputStream[] Input_Stream = null;
    public static DataOutputStream[] Output_Stream = null;  
    public static int[] Connect_Status;
    public static Stack[] Send_Stack = null;
    public static Stack[] Receive_Stack = null;
    public static int[] Connect_Status_With_Other; // 和其他伺服器的連線狀態
    public static DatagramSocket UdpSocket_To_Other;
    public static DatagramPacket UdpPacket_To_Other;


    // 代理伺服器和其他用戶端共用的

    public static int STACK_LEVEL_OF_PROXY_AND_CLIENT = 30; // socket通訊的堆疊深度
    public static int STACK_LEN_OF_PROXY_AND_CLIENT = 20000; // socket通訊的堆疊單位的位元組長度
    public static String suport_path = "C:\\suport"; // 存放檔案和支援檔案的位置


    
   

}
