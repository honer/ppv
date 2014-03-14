/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package service_main_server;

/**此物建用來存放字串,字串的值在此物件裡面為唯一的值
 *
 * @author 廷鴻
 */

public class have_ob {

    String[] have;

    public have_ob(int have_limit)
    {
        have = new String[have_limit];

        for (int i = 0 ; i < have.length ; i++)
        {
            have[i] = "";
        }
    }

    public void Add(String name)
    {
        boolean test = false;
        int last_index = 0;
        for (int i = 0 ; i < have.length ; i++)
        {
            if (!have[i].equalsIgnoreCase(""))
            {
                last_index = i;
                if (have[i].equalsIgnoreCase(name))
                {
                    test = true;
                    break;
                }
            }
        }

        if (test == false) // 尚未有此檔案
            have[last_index + 1] = name;

    }

    public void Del(String name)
    {
        int find_index = -1;
        for (int i = 0 ; i < have.length ; i++)
        {
            if (!have[i].equalsIgnoreCase(""))
                if (have[i].indexOf(name) >= 0)
                {
                    find_index = i;
                    break;
                }
        }

        if (find_index > -1)
        {
            for(int i = find_index ; i < have.length - 1 ; i++)
            {
                have[find_index] = have[find_index + 1];
            }
        }
    }

    public String[] Get() // 讀取目前所有的資料
    {
        return have;
    }


}
