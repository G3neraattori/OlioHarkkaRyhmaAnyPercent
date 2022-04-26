package com.example.olioharkkaryhmaanypercent;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class UserData {
    private static FileWriter file;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String passwordToHash = "password";
        byte[] salt = getSalt();

        String securePassword = get_SHA_256_SecurePassword(passwordToHash, salt, "AA", false);
        //System.out.println(securePassword);
        validatePassword("password", "AA");
        //validatePassword("1234", "AA");

    }

    private static String get_SHA_256_SecurePassword(String passwordToHash, byte[] salt, String username, boolean cym) {

        String generatedPassword = null;
        try {
            JSONObject obj = new JSONObject();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            obj.put("username", username);
            String base64String = Base64.getEncoder().encodeToString(salt);
            obj.put("salt", base64String);
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            obj.put("hash", sb.toString());
            //System.out.println(obj);
            generatedPassword = sb.toString();

            if(cym != true){

                try{
                    JSONParser parser = new JSONParser();
                    JSONArray list = (JSONArray) parser.parse(new FileReader("./database.json"));;
                    list.add(obj);
                    //System.out.println(list);
                    file = new FileWriter("./database.json");
                    file.write(list.toJSONString());
                    file.flush();
                    file.close();
                }catch (IOException e){
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }




        return generatedPassword;
    }

    // Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[64];
        sr.nextBytes(salt);
        System.out.println(salt);
        return salt;
    }

    private static boolean validatePassword(String givenPass, String username) throws NoSuchAlgorithmException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            JSONArray array = (JSONArray) parser.parse(new FileReader("./database.json"));


            for(int i = 0; i < array.size(); i++){
                jsonObject = (JSONObject) array.get(i);
                String name = (String) jsonObject.get("username");
                System.out.print(name+"\n");
                if(name.equals(username)){
                    System.out.print("a\n");
                    String hash = (String) jsonObject.get("hash");
                    String salt = (String) jsonObject.get("salt");

                    byte[] salt2 = Base64.getDecoder().decode(salt);
                    String b = get_SHA_256_SecurePassword(givenPass, salt2, "", true);
                    System.out.print(salt2+"-------------");
                    System.out.println(hash);
                    if (get_SHA_256_SecurePassword(givenPass, salt2, "", true).equals(hash)) {
                        System.out.print("VITTU");
                        //System.out.println(name);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return true;
    }

}
