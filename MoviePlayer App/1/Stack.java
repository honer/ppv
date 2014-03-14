package MoviePlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 廷鴻
 */
public class Stack {

    int stack_point1;
    int stack_point2;
    byte[][] data;
    int [] data_len;
    int level;
    int len;

    Stack(int stack_level,int byte_len){
        level = stack_level;
        len = byte_len;        
        data = new byte[level][len];
        data_len = new int[level];
        stack_point1 = 0;
        stack_point2 = 0;
    }

    public byte[] pop()
    {
        if (stack_point1 - 1 >= 0)
        {
            byte[] rtn = new byte[len];
                rtn = data[0];

            for(int i = 0 ; i < stack_point1 - 1 ; i++)
                data[i] = data[i + 1];

            stack_point1--;

            return rtn;
        }
        else
        {
            byte[] rtn = new byte[1];
            rtn[0] = 0;
            return rtn;
        }
    }

    public int pop_len()
    {
        if(stack_point2 - 1 >= 0)
        {
            int rlen = data_len[0];

            for(int i = 0 ; i < stack_point2 - 1 ; i++)
                data_len[i] = data_len[i + 1];

            stack_point2--;

            return rlen;
        }
        else
            return 0;

    }

    public void push(byte[] stack_data,int len)
    {
        if (stack_point1 < level)
        {
            data[stack_point1] = stack_data;
            stack_point1++;
        }
        if (stack_point2 < level)
        {
            data_len[stack_point2] = len;
            stack_point2++;
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

}
