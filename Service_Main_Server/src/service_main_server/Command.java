/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_main_server;
import java.net.*;
import java.io.*;
/**
 *
 * @author 廷鴻
 */
public class Command {
    
    //和一般使用者用戶端連接使用
    public static int Pre_Work_Index; 
    public static ServerSocket Listen_Socket; //
    public static Socket[] Work_Socket;
    public static int Max_Client_Count = 10000; // 允許連接的數量
    public static int Use_Port = 3388;
    public static InputStream[] Input_Stream = null;
    public static DataOutputStream[] Output_Stream = null;
    public static int Stack_Level = 100; // 通訊的堆碟深度
    public static int Stack_len = 10240; // 通訊的位元陣列的長度
    public static int[] Connect_Status;
    public static Stack[] Send_Stack = null;
    public static Stack[] Receive_Stack = null;
    public static Suports Suport_List = null;

    public static String[] test_ack ; // 用來判斷此連線是否有斷線(看其是否有回應)
    public static boolean[] test_ack_flag;



   
}
