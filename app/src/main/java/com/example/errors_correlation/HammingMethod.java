package com.example.errors_correlation;

import java.util.Map;

public class HammingMethod {
    private static int[] type;
    public static int[] encodeHamming(int[] inputData)
    {
        int inputDataLength = inputData.length;
        int redundancy =0, sum =0;
        int mask =0;
        for(int i=0;i<inputDataLength;)
        {
            if(Math.pow(2,redundancy) - 1 == sum)
                redundancy++;
            else i++;
            sum++;
        }
        int[] codedData= new int[sum];

        type=new int[sum];
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
    public static int[] decodeHamming(int[] codedBits)
    {
        int bitAmount = codedBits.length;
        int d=0;
        int redundancy = 0;

        for(int i=0;i<bitAmount;i++)
        {
            if(Math.pow(2,redundancy) - 1 != i)
                d++;
            else
                redundancy++;
        }
        int[] outputArray = new int[d];
        d=0;
        redundancy = 0;
        for(int i=0;i<bitAmount;i++)
        {
            if(Math.pow(2,redundancy) -1 !=i)
            {
                outputArray[d] = codedBits[i];
                d++;
            }
            else redundancy++;
        }
        return outputArray;
    }
    public static int[] fixHamming(int[] codedBits)
    {
        int length = codedBits.length;
        int[] decodedData;
        int d=0;
        int redundancy =0;
        int errors=0;
        decodedData = codedBits;
        for(int i=0;i<length;i++)
        {
            if(Math.pow(2,redundancy) - 1 !=i)
                d++;
            else redundancy++;
        }

        int mask =0;
        d=0;
        redundancy=0;
        for(int i=0;i<length;i++)
        {
            if(decodedData[i]==1)
                mask^=i+1;
            if(Math.pow(2,redundancy) - 1 != i)
            {
                d++;
                type[i]=0;
            }
            else
            {
                type[i]=3;
                redundancy++;
            }
        }
        if(mask!=0)
        {
            errors++;
            int nr = mask-1;
            if(nr<decodedData.length)
            {
                if(type[nr]==0)
                    type[nr]=1;
                else if(type[nr]==3)
                    type[nr]=4;
                if(decodedData[nr]==1)
                    decodedData[nr]=0;
                else decodedData[nr]=1;
            }
        }
        return decodedData;
    }

}
