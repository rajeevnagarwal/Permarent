package com.furniture.appliances.rentals.util;

import java.util.Random;

/**
 * Created by Infinia on 04-10-2015.
 */
public class GenerateOTP {
    public static String OTP(int min, int max)
    {
        Random rand = new Random();
        return String.valueOf(rand.nextInt((max - min) + 1) + min);
    }
}
