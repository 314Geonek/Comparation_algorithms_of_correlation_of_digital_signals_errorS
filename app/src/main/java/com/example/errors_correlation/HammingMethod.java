package com.example.errors_correlation;

import java.util.Collections;
import java.util.List;

public class HammingMethod {

    private static int counterOfRedundantBits;
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
                for(int k=i; k<inputList.size(); k+=2*i){
                    for(int m=0;m<i && k+m <inputList.size();m++)
                        if(inputList.get(k+m)!=null) {
                            value += inputList.get(k + m);
                        System.out.println("Do i="+i+" dodaje g="+(k+m) + " o wartosci:"+inputList.get(m+k));}
                   }
                value = value%2 == 0 ? 0 : 1;
                inputList.set(i, value);
            }
        }
    }


}
