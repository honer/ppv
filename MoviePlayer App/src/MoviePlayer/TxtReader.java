package MoviePlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.net.*;
import java.io.*;

public class TxtReader {
	String File1;
	TxtReader(String File){
		File1 = File;
	}
	public String getData(){
		try{
			FileReader fr = new FileReader(File1);
			BufferedReader br = new BufferedReader(fr); 
		
			int i=0;
			String ss;
            String all = "";
			do{
				ss = br.readLine();
				all += ss + "\n";
				i++;
			}while(ss!= null);

            return all;
		}catch(IOException e){
			return "";
		}
	}		
	 
	
}