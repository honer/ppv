package MoviePlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */
public class Get_Init_Data extends Thread {

    @Override
    public void run()
    {

        try
        {

            Command.Keyin_To_Main =  "getlist1";

            while(Command.list1 == null){
                Thread.sleep(100);
            }
       
 

        }catch(Exception ex)
        {
             //System.out.println("Get_Init_Data");
             //System.out.println(ex.toString());
        }








    }
}
