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
public class Main {

  

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {                
        
        try{            
             init(); // 伺服器初始化
             init_list(); // 初始化支援的清單
                                  
             Listen_Thread Thread0 = new Listen_Thread();
             Receive_Thread Thread1 = new Receive_Thread();
             Send_Thread Thread2 = new Send_Thread();
             Manager_Thread Thread3 = new Manager_Thread();

             Thread0.start();       // 等待其他使用者連線
             Thread1.start();
             Thread2.start();
             Thread3.start();

        }catch(Exception e){
             System.out.print("main");
            System.out.print(e.toString());
            System.exit(0);
        } 
                 
    }

    public static void init()
    {
      try{
             Command.Listen_Socket = new ServerSocket(Command.Use_Port);
             Command.Work_Socket = new Socket[Command.Max_Client_Count];
             Command.Connect_Status = new int[Command.Max_Client_Count];
             Command.Receive_Stack = new Stack[Command.Max_Client_Count];
             Command.Send_Stack = new Stack[Command.Max_Client_Count];
             Command.Input_Stream = new InputStream[Command.Max_Client_Count] ;
             Command.Output_Stream = new DataOutputStream[Command.Max_Client_Count];
             Command.test_ack = new String[Command.Max_Client_Count];
             Command.test_ack_flag = new boolean[Command.Max_Client_Count];

             System.out.println("伺服器初始化成功!等待用戶端連接!");

        }catch(Exception e){
             System.out.println("init");
             System.out.print(e.toString());
             System.exit(0);
        }

    }

    public static void init_list() // 載入目前支援的清單
    {
      try{
                 Command.Suport_List =  new Suports(1000,1000);

                 File s = new File("\\list");
                 String[] list = s.list();
                 
                 for (int i = 0 ; i < list.length ; i++)
                 {
                    TxtReader tx = new TxtReader("\\list\\" +list[i]);
                    String list2 = tx.getData();
                    String[] list3 = list2.split("\n");

                    for (int j = 0 ; j < list3.length ; j++)
                        if (list3[j] != null)
                            if(list3[j].indexOf(" ") >= 0)
                            {
                                String[] ss = list3[j].split(" ");
                                Command.Suport_List.Add_Suport_File(ss[1],Integer.parseInt(ss[2]));
                            }
                 }


        }catch(Exception e){
             System.out.println("init_list");
             System.out.print(e.toString());

        }

    }



}
