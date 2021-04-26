package com.example.errors_correlation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HammingMethod {
    private static int mask;
    private static int redundancy;
    private static int sum;
    public static List<Integer> encodeHamming(List<Integer> inputData)
    {
        List<Integer> codedData= new ArrayList<>();
        sum = 0;
        redundancy = 0;
        mask = 0;
        for(int i = 0; i<inputData.size();)
        {
            if(Math.pow(2,redundancy) - 1 == sum)
                redundancy++;
            else {
                System.out.println(sum +"  " +   inputData.get(i));
                codedData.add(sum, inputData.get(i)) ;
                if(inputData.get(i)==1)
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
                    codedData.set(i , 0);
                else
                    codedData.set(i, 1);
                redundancy++;
            }
        }
        return codedData;
    }
    public static int[] decodeHamming(List<Integer> codedBits)
    {
        int length = codedBits.size();
        int d=0;
        int redundancy = 0;

        for(int i=0;i<length;i++)
        {
            if(Math.pow(2,redundancy) - 1 != i)
                d++;
            else
                redundancy++;
        }
        int[] outputArray = new int[d];
        d=0;
        redundancy = 0;
        for(int i=0;i<length;i++)
        {
            if(Math.pow(2,redundancy) -1 !=i)
            {
                outputArray[d] = codedBits.get(i);
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
             //   type[i]=0;
            }
            else
            {
         //       type[i]=3;
                redundancy++;
            }
        }
//        if(mask!=0)
//        {
//            errors++;
//            int nr = mask-1;
//            if(nr<decodedData.length)
//            {
////                if(type[nr]==0)
////                    type[nr]=1;
////                else if(type[nr]==3)
////                    type[nr]=4;
////                if(decodedData[nr]==1)
////                    decodedData[nr]=0;
////                else decodedData[nr]=1;
//            }
//        }
        return decodedData;
    }

}
