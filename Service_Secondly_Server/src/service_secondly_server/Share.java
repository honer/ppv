package service_secondly_server;
import java.net.*;
import java.io.*;
import java.net.*;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */
public class Share {
        static int get_proxy_index(String IP) // 根據代理伺服器的IP,找出在運作中的索引編號
        {
             for (int i = 0 ; i < Command.pre_sup_proxy_count; i++)
                 if (Command.IP_Of_Proxy[i] == IP)
                     return i;

             return -1;
        }

         //  以下程式是要找出代理伺服器的對外真實IP
        public static String get_real_ip()
        {
            try
            {

                StringBuffer document = new StringBuffer();
                URL url = new URL(" http://checkip.dyndns.org ");
                URLConnection conn = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null)
                document.append(line + " ");
                reader.close();

                String myip = document.substring(document.indexOf(": ") + 2, document.indexOf("</body>"));

                return myip;

            }
            catch(Exception ex)
            {
                System.out.println("get_real_ip");
                System.out.println(ex.toString());
                return "";
            }
        }



}
