/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MoviePlayer;
import java.net.*;
import java.io.*;
import java.net.*;
/**
 *
 * @author Administrator
 */
public class File_Writer {

    public static int Write(String File_Path, byte[] data,int length,boolean is_continue)
    {

        try{

            FileOutputStream fo = new FileOutputStream(File_Path,is_continue);

            fo.write(data, 0, length);

            fo.close();

            return data.length;
		}
        catch(Exception e){
			//System.out.println("File_Writer");
            //System.out.println(e.toString());
            return 0;
        }

    }

}
