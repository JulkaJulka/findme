package com.findme;

public class Utils {
    public static boolean checkWordOnDigts(String body) throws BadRequestException {
        char[] chars = body.toCharArray();
        for (char ch : chars) {
            if (!Character.isDigit(ch))
               throw new BadRequestException("Wrong data input");
        }
        return true;
    }
}
