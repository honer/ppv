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
 * @author Administrator
 */
public class File_Reader {

    public static byte[] get_bytes(String file_path,int start_index,int length)
    {
    		try{
                
                FileInputStream fi = new FileInputStream(file_path);

                fi.skip(start_index);

                byte[] bte = new byte[length];

                fi.read(bte, 0, bte.length);

                //byte[] bte2 = new byte[length];

                //int index = 0;
                //for (int i = start_index ; i < (start_index + length) ; i++)
                //    bte2[index++] = bte[i];

               // Command.finish_rec_file_Length +=length ; // 累積已經回傳完畢的檔案長度

                 return bte;

		}catch(Exception e){
			System.out.println("File_Reader");
            System.out.println(e.toString());
            byte[] bte = new byte[1];
            bte[0] = 0;
            return bte;
        
        }

   }

}
