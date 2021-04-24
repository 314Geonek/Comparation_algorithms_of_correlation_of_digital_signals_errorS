package com.example.errors_correlation;

import java.util.Map;

public class HummingMethod {
    public static int[] encodeHumming(int[] inputData)
    {
        int inputDataLength = inputData.length;
        int redundancy =0, sum =0;
        int[] codedData=new int[sum];
        int mask =0;
        for(int i=0;i<inputDataLength;)
        {
            if(Math.pow(2,redundancy) - 1 == sum)
                redundancy++;
            else i++;
            sum++;
        }
        sum = 0;
        redundancy = 0;

        for(int i=0;i<inputDataLength;)
        {
            if(Math.pow(2,redundancy) - 1 == sum)
                redundancy++;
            else {
                codedData[sum] = inputData[i];
                if(inputData[i]==1)
                    mask ^=sum +1;
                i++;
            }
            sum++;
        }
        redundancy = 0;
        for(int i=0; i<sum; i++)
        {
            if(Math.pow(2, redundancy) -1 == i)
            {
                if((mask & (1<<redundancy))==0)
                    codedData[i]=0;
                else
                    codedData[i]=1;
                redundancy++;
            }
        }
        return codedData;
    }
}
