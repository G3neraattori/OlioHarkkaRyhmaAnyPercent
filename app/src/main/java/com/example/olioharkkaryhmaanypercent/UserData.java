package com.example.olioharkkaryhmaanypercent;

//import org.json.JSONObject;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;


public class UserData extends MainActivity{
    private static OutputStreamWriter file;
    private static Context context;
//    public USER user;

    public UserData(Context context) {
        this.context = context;
        //this.user = new USER(this.context);

    }

    //Tekee käyttäjän
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean createUser(String username, String passwordToHash) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String generated = getPass(passwordToHash, salt, username, false);
        Toast.makeText(context, "Käyttjänimeä, ei löytynyt. Luodaan uusi käyttäjä.", Toast.LENGTH_SHORT).show();

        return generated != null;
    }


    //Luo ja uudelleen luo hashejä.
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPass(String passwordToHash, byte[] salt, String username, boolean notCreating) {
        File asset = new File(context.getFilesDir().getPath()+"database.json");
        //Ensimmäisellä asennuksella kopio database.json assettin
        if(!asset.exists()){
            try {
                copydatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String generatedPassword = null;
        //Tekee uuden olion .json
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
            generatedPassword = sb.toString();

            if(!notCreating){

                try{
                    JSONParser parser = new JSONParser();
                    
                    BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));
                    String line;
                    while((line = br.readLine()) != null){
                        System.out.println(line);
                    }
                    JSONArray list = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));
                    System.out.println(list);
                    list.add(obj);
                    file = new OutputStreamWriter(new FileOutputStream(context.getFilesDir().getPath()+"database.json"));
                    file.write(list.toJSONString());
                    file.flush();
                    file.close();
                }catch (IOException | ParseException e){
                    e.printStackTrace();
                }

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    private void copydatabase() throws IOException {
         final String path = context.getFilesDir().getPath();
         final String Name = "database.json";

        OutputStream os = new FileOutputStream(path + Name);
        byte[] buffer = new byte[1024];
        int length;
        InputStream is = context.getAssets().open("database.json");
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();

    }

    // Lisää salt
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[64];
        sr.nextBytes(salt);
        return salt;
    }

    //Returnaa TRUE jos löytyi käyttäjä false jos ei
    //TALLENTAA VAIN PUHELIMEEN PITÄISI MUUTTAA TALLENTAMAAN PALVELIMELLE MUTTA MEILLÄ EI OLE SIIHEN RESURSSEJÄ.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validatePassword(String givenPass, String username) throws NoSuchAlgorithmException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));
            for(int i = 0; i < array.size(); i++){
                jsonObject = (JSONObject) array.get(i);
                String name = (String) jsonObject.get("username");
                System.out.print("Searching for: " + name + " with password: " + givenPass + "\n");
                if(name.equals(username)){
                    System.out.print("Username found\n");
                    String hash = (String) jsonObject.get("hash");
                    String salt = (String) jsonObject.get("salt");

                    byte[] salt2 = Base64.getDecoder().decode(salt);
                    String b = getPass(givenPass, salt2, "", true);
                    System.out.println(salt2+"-------------"+hash);
                    System.out.println(b);
                    if (getPass(givenPass, salt2, "", true).equals(hash)) {
                        Toast.makeText(context, "Kirjauduttu sisään.", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getUser(String username){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try {
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));

            for(int i = 0; i < array.size(); i++){
                jsonObject = (JSONObject) array.get(i);
                String name = (String) jsonObject.get("username");
                System.out.println("Searching for: " + username);
                if(name.equals(username)){
                    System.out.print("Username found\n");
                    return true;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("Searching for: " + username);
        return false;
    }

    //lataa käyttäjädatan. tämäkin olisi normaalisti palvelimella mutta enemmän proof on concept
    public void loadUserData(String username){
        File asset = new File(context.getFilesDir().getPath()+"userdata"+username+".json");
        //Ensimmäisellä asennuksella kopio userdata.json assettin
        if(!asset.exists()){
            try {
                final String path = context.getFilesDir().getPath();
                final String Name = "userdata"+username+".json";

                OutputStream os = new FileOutputStream(path + Name);
                byte[] buffer = new byte[1024];
                int length;
                InputStream is = context.getAssets().open("userdata.json");
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.flush();
                os.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //TODO tarvitsee sen miten data esitetetään käyttäjäsivulla. Luultavasti parsee jsonista kaiken siihhen sivulle kivasti

    }

    public static HashMap<String, Movie> actuallyLoadUserData(String username){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try {
            HashMap<String, Movie> movieList = new HashMap<String, Movie>();
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"userdata"+username+".json")));

            for(int i = 0; i < array.size(); i++){
                jsonObject = (JSONObject) array.get(i);
                JSONObject movieObject;
                movieObject = (JSONObject) jsonObject.get("Movie");
                System.out.println(movieObject);
                System.out.println(movieObject.get("name"));
                movieList.put(movieObject.get("id").toString(), new Movie(movieObject.get("name").toString(), Integer.parseInt(movieObject.get("id").toString()), movieObject.get("description").toString(), "", null, 0, Integer.parseInt(movieObject.get("year").toString()), "") );
                System.out.println(movieObject.get("name").toString());

            }
            System.out.println(Arrays.asList(movieList));
            return movieList;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void checkIfMovieFavourite(String username, String movieName) {
        try{
            JSONParser parser = new JSONParser();

            BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));
            String line;
            while((line = br.readLine()) != null){
                System.out.println(line);
            }
            JSONArray list = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));

            file = new OutputStreamWriter(new FileOutputStream(context.getFilesDir().getPath()+"database.json"));
            file.write(list.toJSONString());
            file.flush();
            file.close();
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }

    }
    //, int personal_rating
    public static void saveMovie(USER user, Movie movie){
        System.out.println("Username1:" + user.getUsername());
        System.out.println("Moviename1: " + movie.getMovieName());
        JSONObject obj = new JSONObject();
        try{
            JSONParser parser = new JSONParser();

            BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"userdata"+user.getUsername()+".json")));
            String line;
            while((line = br.readLine()) != null){
                System.out.println(line);
            }
            JSONArray list = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"userdata"+user.getUsername()+".json")));
            //movie.setpersonalRating(personal_rating);
            obj.put("name", movie.getMovieName());
            obj.put("id", movie.getMovieID());
            obj.put("description", movie.getMovieDescription());
            obj.put("year", movie.getMovieYear());
            obj.put("imdb", movie.getImdbRating());
            obj.put("personal", movie.getPersonalRating());



            JSONObject movieObj = new JSONObject();
            movieObj.put("Movie", obj);
            list.add(movieObj);

            file = new OutputStreamWriter(new FileOutputStream(context.getFilesDir().getPath()+"userdata"+user.getUsername()+".json"));
            file.write(list.toJSONString());
            file.flush();
            file.close();
        }catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public void saveReview(USER user, Movie movie, String moviename, int review){
        System.out.println("Username1:" + user.getUsername());
        System.out.println("Moviename1: " + movie.getMovieName());
        JSONParser parser = new JSONParser();

        JSONObject jsonObject;
        try {
            JSONArray list = new JSONArray();
            HashMap<String, Movie> movieList = new HashMap<String, Movie>();
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"userdata"+user.getUsername()+".json")));

            for(int i = 0; i < array.size(); i++){
                jsonObject = (JSONObject) array.get(i);
                JSONObject movieObject;
                movieObject = (JSONObject) jsonObject.get("Movie");
                if(movieObject.get("name").toString().equals(moviename)){
                    movieObject.put("personal", review);
                    jsonObject.put("Movie", movieObject);
                    list.add(jsonObject);
                }else{
                    list.add(jsonObject);
                }

            }




            file = new OutputStreamWriter(new FileOutputStream(context.getFilesDir().getPath()+"userdata"+user.getUsername()+".json"));
            file.write(list.toJSONString());
            file.flush();
            file.close();


        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public static class USER extends UserData{
        boolean newUser;
        String username;

        public USER(Context context) {
            super(context);
        }

        public String getUsername() {
            return this.username;
        }

        public void setStatus(boolean status){
            this.newUser = status;
        }

    }


}
