package com.example.errors_correlation;

import androidx.core.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HammingMethod {

    private static int counterOfRedundantBits;
    private static List<Integer> errorList;
    public static int getCounterOfRedundantBits() {
        return counterOfRedundantBits;
    }
    private static void findCounterOfRedundantBits(int inputDataLength)
    {   counterOfRedundantBits = 0;
        while (Math.pow(2, counterOfRedundantBits) < (inputDataLength + counterOfRedundantBits + 1)) {
            counterOfRedundantBits++;
        }
    }
    public static void calcHuffman(List<Integer> inputList)
    {   findCounterOfRedundantBits(inputList.size());
        Collections.reverse(inputList);
        inputList.add(0,0); //empty bit add remove at the end
        for(int i=0;i<counterOfRedundantBits;i++)
        {
            inputList.add((int)Math.pow(2,i), null);
        }
        calcAndFillValueOfRedundantBits(inputList);
        inputList.remove(0);
        Collections.reverse(inputList);
    }
    public static void calcAndFillValueOfRedundantBits(List<Integer> inputList)
    { for(int i = 1; i < inputList.size(); i++)
        {
            if(inputList.get(i)==null)
            {   int value =0;
                for(int k=i; k<inputList.size(); k+=2*i)
                    for(int m=0;m<i && k+m <inputList.size();m++)
                        if(inputList.get(k+m)!=null)
                            value += inputList.get(k + m);

                value = value%2 == 0 ? 0 : 1;
                inputList.set(i, value);
            }
        }
    }

    public static void decodeHamming(List<Integer> inputList)
    {
        Collections.reverse(inputList);
        inputList.add(0,null);
        fixHamming(inputList);
        for(int i=counterOfRedundantBits-1;i >= 0;i--)
        {   inputList.remove( (int) Math.pow(2,i)); }
        inputList.remove(0);
        Collections.reverse(inputList);
    }
    private static void fixHamming(List<Integer> inputList)
    {   List<Integer> redundantBitsIndexes = new ArrayList<>();
        errorList = new ArrayList<>();
        for(int i=0;i<counterOfRedundantBits;i++)
            redundantBitsIndexes.add((int) Math.pow(2,i));
        redundantBitsIndexes.forEach(redundantBit ->{
           int counterOfCurrentBit=0;
            for(int i=redundantBit; i<inputList.size(); i++)
            {
                for(int j=0;j<redundantBit;j++)
                    counterOfCurrentBit += inputList.get(i+j);
            }
            if(counterOfCurrentBit % 2 == 1)
                errorList.add(redundantBit);
        });
        int liedBid = errorList.stream().mapToInt(i -> i).sum();
        if(inputList.get(liedBid)==1)
            inputList.set(liedBid,0);
        else inputList.set(liedBid,1);
    }
}
