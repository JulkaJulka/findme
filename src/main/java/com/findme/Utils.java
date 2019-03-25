package com.findme;

public class Utils {
    public static boolean checkWordOnDigts(String id) throws BadRequestException {
       try{
           Long idLong = Long.valueOf(id);
           return true;
       } catch (NumberFormatException e){
           throw new BadRequestException("Wrong data input");
       }
    }
}
