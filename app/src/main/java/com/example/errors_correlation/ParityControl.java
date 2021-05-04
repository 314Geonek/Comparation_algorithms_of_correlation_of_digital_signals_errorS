package com.example.errors_correlation;

import java.util.ArrayList;
import java.util.List;

public class ParityControl{
    private static int counterOfDetectedDistortions; // w kontorli parzystości 1 lub 0
    private static final int counterOfControlBits = 1;
    public static int getCounterOfControlBits() {
        return counterOfControlBits;
    }
    public static void encode(List<Byte> bitsList)
    {
        int sum = bitsList.stream().mapToInt(i -> i).sum();
        if(sum % 2 == 0)
            bitsList.add(0, (byte) 1);
        else bitsList.add(0, (byte) 0);
    }
    public static void decode(List<Byte> bitsList)
    {
        int sum = bitsList.stream().mapToInt(i -> i).sum();
        counterOfDetectedDistortions = sum % 2 == 0 ? 1 : 0;
        bitsList.remove(0);
    }

    public static int getCounterOfDetectedDistortions() {
        return counterOfDetectedDistortions;
    }
}
