package service_main_server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.*;
import java.io.*;
/**
 *
 * @author 廷鴻
 */
public class Manager_Thread extends Thread{

    int count = 0;

    @Override
    public void run()
    {
       
        while(true)
        {                
            try{
                Update_Connect_State(); // 更新目前的連線狀態

                Check_Instruction(); //判斷從客戶端收到的指令

                Thread.sleep(100);

               }catch(Exception e){
                    System.out.println("Manager_Thread");
                    System.out.println(e.toString());
               }
        }

    }

    void Update_Connect_State() // 更新目前的連線狀態
    {
       // int ii = 0;

        try{
            count ++;

      //System.out.println("count:" + count);
            if (count == 1)
            {
//System.out.println("count>>>1");
                for(int i = 0 ; i < Command.Max_Client_Count; i++)
                {
               // ii = i;
                    if (Command.Connect_Status[i] == 1)
                    {
                        Command.test_ack[i] = "";
                        Command.test_ack_flag[i] = true;
                        String send ="#$ins_connect";
                        Command.Send_Stack[i].push(send.getBytes(),send.getBytes().length);
                        System.out.println("傳送保持連線測試指令");
                    }

                }
            }

            if (count == 50)
            {
                //System.out.println("count>>>>>50");
                for(int i = 0 ; i < Command.Max_Client_Count; i++)
                {
                    if (Command.Connect_Status[i] == 1 &&  Command.test_ack_flag[i])
                    {
                        Command.test_ack_flag[i] = false;

                        if(Command.test_ack[i] != null)
                            if (!Command.test_ack[i].equalsIgnoreCase("ok"))
                            {
                                Command.Suport_List.Del_Suport_Server(Command.Work_Socket[i].getInetAddress().toString());
                                System.out.println("客戶端: " + i + " IP:" + Command.Work_Socket[i].getInetAddress().toString() + " 離線了");
                                Command.Connect_Status[i]  = 0;
                                // 試放資源
                                Command.Work_Socket[i].shutdownInput();
                                Command.Work_Socket[i].shutdownOutput();
                                Command.Work_Socket[i].close();
                            }
                            else
                                System.out.println("客戶端: " + i + " IP:" + Command.Work_Socket[i].getInetAddress().toString() + " 目前保持連線");
                    }
                }

            count = 0;
            }

          }catch(Exception e){
                System.out.println("Update_Connect_State");
                System.out.println(e.toString());
                //System.out.println("count:" + count);
                   //  System.out.println("ii:" + ii);
          }
    }

    void Check_Instruction()  // 判斷重客戶端收到的指令
    {
        try{

            for(int i = 0 ; i < Command.Max_Client_Count ; i++)
            {
                   if (Command.Connect_Status[i] == 1 )
                   {
                       //System.out.println("查看連線 " + i + " 有無資料");

                      int len = Command.Receive_Stack[i].pop_len();
                      byte[] bte = Command.Receive_Stack[i].pop();

                      if (len > 0)
                      {

                            String str = new String(bte,0,len);

                            System.out.println("接收到連線 " + i + " 的資料:" +str);

                            if (str.indexOf("#$ins") >= 0) //  此為指令封包
                            {
                                String[] ins = str.split("_");

                                if(ins[1].equalsIgnoreCase("get")) // 接收到要求清單的指令
                                {
                                    File s = new File("\\list");

                                    String[] list = s.list();

                                    String ss = "";

                                    for(int j = 0; j < list.length ; j++)
                                    {
                                        ss += list[j].replace(".txt", "") + "\n";
                                    }

                                    byte send[] = ss.getBytes();
                                    Command.Send_Stack[i].push(send, send.length);
                                    System.out.println("回傳選單");
                                    System.out.println(ss);
                                }
                                else if(ins[1].equalsIgnoreCase("list")) // 節目選單的指令
                                {
                                    File s = new File("\\list");
                                    String[] list = s.list();
                                    TxtReader tx = new TxtReader("\\list\\" +list[Integer.parseInt(ins[2])]);
                                    String list2 = tx.getData();
                                   // String[] list3 = list2.split("\n");
                                   // String list4 = "";

                                   // for (int j = 0; j < list3.length ; j++)
                                   // {
                                   //     list4 += list3[j].split(" ")[0] + "\n";
                                   // }

                                    byte send[] = list2.getBytes();
                                    Command.Send_Stack[i].push(send, send.length);
                                    System.out.println("回傳節目");

                                }else if(ins[1].equalsIgnoreCase("suport")) // 節目選單的指令
                                {
                                    if (ins.length >= 4)
                                    {
                                        if (ins[3].indexOf("@") >= 0)
                                        {
                                            String[] suport_file = ins[3].split("@"); // 分析出所有支援的檔名

                                            for (int j = 0 ; j < suport_file.length ; j++)
                                            {
                                                if (!suport_file[j].equalsIgnoreCase(""))
                                                    Command.Suport_List.Add_Suport_Server(suport_file[j], ins[2]);
                                            }
                                        }
                                    }
                                }
                                else if(ins[1].equalsIgnoreCase("suportget")) // 傳來檔案名子,回傳檔案大小和支援的伺服器列表
                                {
                                    String size = String.valueOf(Command.Suport_List.Get_Suport_File_size(ins[2]));

                                    String send2 = "ok_" + size;
                                    Command.Send_Stack[i].push(send2.getBytes(), send2.getBytes().length);
                                    System.out.println("回傳影片大小");

                                    Thread.sleep(50); // 防止回傳的東西混在一起

                                    String[] ll = Command.Suport_List.Get_Suport_List(ins[2]);

                                    String dd = "";

                                    for(int j = 0 ; j < ll.length ; j++)
                                    {
                                      if(!ll[j].equalsIgnoreCase("") ) dd += ll[j] + "\n";
                                    }

                                    byte send[] = dd.getBytes();
                                    Command.Send_Stack[i].push(send, send.length);
                                    System.out.println("回傳影片支援列表");

                                }
                                else if(ins[1].equalsIgnoreCase("ok")) // 接收到要求清單的指令
                                {
                                    Command.test_ack[i] = "ok";
                                }
                            }
                            else // 此為資料
                            {


                            }
                        }
                   }
                }

              }catch(Exception e){
                    System.out.println("Check_Instruction");
                    System.out.println(e.toString());
               }
        }
}
