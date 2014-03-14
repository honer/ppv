package service_main_server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */
public class Suports {

    public String[] Suport_File;      // 為支援的檔案名
    public int[] Suport_File_Size; // 為支援的檔案的大小
    public have_ob[] Suport_List; // 前面為支援的檔案名的索引,後面為支援的伺服器ip@port列表;


    public Suports(int file_limit,int server_limit)
    {
        Suport_File = new String[file_limit];
        Suport_File_Size = new int[file_limit];
        Suport_List = new have_ob[file_limit];

        for (int i = 0 ; i < file_limit ; i++)
        {
            Suport_File[i] = "";
            Suport_File_Size[i] = 0;
            Suport_List[i] = new have_ob(server_limit);
        }
    }

    public void Add_Suport_File(String file_name,int file_size)
    {
        boolean test = false;
        int last_index = 0;
        for (int i = 0 ; i < Suport_File.length ; i++)
        {
            if (!Suport_File[i].equalsIgnoreCase(""))
            {
                last_index = i;
                if (Suport_File[i].equalsIgnoreCase(file_name))
                {
                    test = true;
                    break;
                }
            }
        }

        if (test == false) // 尚未有此檔案
        {
            Suport_File[last_index + 1] = file_name;
            Suport_File_Size[last_index + 1] = file_size;
        }
   }

    public void Del_Suport_File(String file_name)
    {
     int find_index = -1;
        for (int i = 0 ; i < Suport_File.length ; i++)
        {
            if (!Suport_File[i].equalsIgnoreCase(""))
                if (Suport_File[i].indexOf(file_name) >= 0)
                {
                    find_index = i;
                    break;
                }
        }

        if (find_index > -1)
        {
            for(int i = find_index ; i < Suport_File.length - 1 ; i++)
            {
                Suport_File[find_index] = Suport_File[find_index + 1];
                Suport_File_Size[find_index] = Suport_File_Size[find_index + 1];
            }
        }

    }

    public void Add_Suport_Server(String file_name,String server_ip_port) // 增加支援的代理伺服器ip@port
    {
        for (int i = 0 ; i < Suport_File.length ; i++)
        {
            if (Suport_File[i].equalsIgnoreCase(file_name))
            {
                Suport_List[i].Add(server_ip_port);
            }
        }
    }

    public void Del_Suport_Server(String server_ip)
    {
        for (int i = 0 ; i < Suport_List.length ; i++)
                Suport_List[i].Del(server_ip);              
    }

    public String[] Get_Suport_List(String file_name)
    {

        for (int i = 0 ; i < Suport_File.length ; i++)
        {
            if (Suport_File[i].equalsIgnoreCase(file_name))
            {
               return Suport_List[i].Get();
            }
        }

        String[] dd = new String[1];
        dd[0] = "";
        return dd;
    }

    public int Get_Suport_File_size(String file_name)
    {
        for (int i = 0 ; i < Suport_File.length ; i++)
        {
            if (!Suport_File[i].equalsIgnoreCase(""))
            {
                if (Suport_File[i].equalsIgnoreCase(file_name))
                {
                   return Suport_File_Size[i];
                }
            }
        }
        
        return 0;

    }

}
