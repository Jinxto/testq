package com.discord;

import java.util.Random;

public class utilities {
   public String generateRandomString(int length) {
	   String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
       StringBuilder salt = new StringBuilder();
       Random rnd = new Random();
       while (salt.length() < length) { // length of the random string.
           int index = (int) (rnd.nextFloat() * SALTCHARS.length());
           salt.append(SALTCHARS.charAt(index));
       }
       String saltStr = salt.toString();
       return saltStr;
   }
}
