package com.example.errors_correlation;

import java.util.ArrayList;
import java.util.List;

public class ParityControl{
    private static int counterOfDetectedDistortions;
    private static final int counterOfControlBits = 1;
    public static int getCounterOfControlBits() {
        return counterOfControlBits;
    }
    public static void encode(List<Byte> bitsList)
    {
        int counter = (int) bitsList.stream().filter(bit  -> bit == 1).count();
        bitsList.add(0, counter % 2 == 0 ? (byte) 0 : (byte) 1);
    }
    public static void decode(List<Byte> bitsList)
    {
        int counter = (int) bitsList.stream().filter(bit  -> bit == 1).count();
        counterOfDetectedDistortions = counter % 2 == 0 ? 0 : 1;
        bitsList.remove(0);
    }

    public static int getCounterOfDetectedDistortions() {
        return counterOfDetectedDistortions;
    }
}
