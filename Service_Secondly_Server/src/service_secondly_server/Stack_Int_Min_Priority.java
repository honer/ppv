package service_secondly_server;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */
public class Stack_Int_Min_Priority {

    int stack_point1;
    int stack_point2;
    int[] length;
    int[] startindex;
    int level;

    Stack_Int_Min_Priority(int stack_level){
        level = stack_level;
        length = new int[level];
        startindex = new int[level];
        stack_point1 = 0;
        stack_point2 = 0;
    }

    public int pop_length()
    {
        if (stack_point2 - 1 >= 0)
        {
            int dd = length[0];

            for(int i = 0 ; i < stack_point2 - 1 ; i++)
                length[i] = length[i + 1];

            stack_point2--;

            return dd;
        }
            return 0;
    }

    public int pop_startindex()
    {
        if (stack_point1 - 1 >= 0)
        {
            int dd = startindex[0];

            for(int i = 0 ; i < stack_point1 - 1 ; i++)
                startindex[i] = startindex[i + 1];

            stack_point1--;

            return dd;
        }
            return 0;
    }

    public void push(int send_startindex,int send_length)
    {
         if (stack_point1 < level)
         {
            startindex[stack_point1] = send_startindex;
            stack_point1++;
         }

        if (stack_point2 < level)
        {
            length[stack_point2] = send_length;
            stack_point2++;
        }
     
        //*** 排序,將最小的值拿到最前面,最大在最後面

        // 使用泡末排序法
         boolean sort = true;
         int temp;

         while(sort)
         {
            sort = false;
            for(int i = 0 ; i < stack_point1 - 1 ; i++)
            if (startindex[i + 1] < startindex[i])
            {
                 temp = startindex[i];
                 startindex[i] = startindex[i + 1];
                 startindex[i + 1] = temp;

                 temp = length[i];
                 length[i] = length[i + 1];
                 length[i + 1] = temp;


                 sort = true;
            }
         }
    }

    public boolean is_data1_full()
    {
        if (stack_point1 == level)
            return true;
        else
            return false;
    }


    public boolean is_data2_full()
    {
        if (stack_point2 == level)
            return true;
        else
            return false;
    }

    public int pre_point1()
    {
        return stack_point1;
    }
}
