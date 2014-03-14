/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_secondly_server;
import java.net.*;
import java.io.*;
import java.net.*;
/**
 * 此執行緒是為了接收外端輸入控制命令的
 * @author 廷鴻
 */
public class Keyin_Manerger_Thread extends Thread {


    @Override
    public void run()
    {
        try{



            while(true)
            {

                keyin_to_main(); // 入要傳到主伺服器的資料輸
              
               // Send_To_Porxy(); // 傳送資料到代理伺服器
               // Send_To_Other(); // 自己作為代理司服器傳送資料到其他的用戶端


                Thread.sleep(100);
            }

        }catch(Exception e){
            System.out.println("Manerger_Thread");
            System.out.println(e.toString());

        }
    }


    //傳送的資料格式為: 用戶端的編號_要傳送的資料
    void Send_To_Other() // 自己作為代理司服器傳送資料到其他的用戶端
    {
    try{

            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader buffereader = new BufferedReader(reader);

            System.out.println("輸入要傳送到其他用戶端的字串");
            String str = buffereader.readLine();

            if (str.split("_").length >= 2)
            {
                int num =Integer.parseInt(str.split("_")[0]);
                String send = str.split("_")[1];
                Command.Send_Stack[num].push(send.getBytes(), send.getBytes().length);
                System.out.println("傳送字串:" + send + "其他用戶端" + num);
            }

        }catch(Exception e){
             System.out.println("Send_To_Other");
             System.out.println(e.toString());
        }
    }

   //傳送的資料格式為: 代理伺服器的編號_要傳送的資料
    void Send_To_Porxy() // 傳送資料到代理伺服器
    {
        try{

            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader buffereader = new BufferedReader(reader);

            System.out.println("輸入要傳送到代理伺服器的字串");
            String str = buffereader.readLine();



            if (str.split("@").length >= 2)
            {
                int num =Integer.parseInt(str.split("@")[0]);
                String send = str.split("@")[1];
                Command.Send_Stack_In_Proxy[num].push(send.getBytes(), send.getBytes().length);
                System.out.println("傳送字串:" + send + "到代理伺服器" + num);
            }

        }catch(Exception e){
             System.out.println("Send_To_Porxy");
             System.out.println(e.toString());
        }
    }

    //如果打入getmovies指令,會批次傳送要求的電影檔名到主伺服器
    //並根據回傳有支援此電影檔名的伺服器列表,開始和其它的代理伺服器建立連接
    //

    void keyin_to_main()
    {

        try
        {

            InputStreamReader reader = new InputStreamReader(System.in);
            BufferedReader buffereader = new BufferedReader(reader);

            System.out.println("輸入要傳送到主伺服器的字串");
            String str = buffereader.readLine();

            Command.Keyin_To_Main = str;

        }
        catch(Exception ex)
        {
            System.out.println("keyin_to_main");
            System.out.println(ex.toString());
        }
    }
}
