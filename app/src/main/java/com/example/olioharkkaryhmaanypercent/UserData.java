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

    //Creates a new user
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean createUser(String username, String passwordToHash) throws NoSuchAlgorithmException {
        byte[] salt = getSalt();
        String generated = getPass(passwordToHash, salt, username, false);
        Toast.makeText(context, "Käyttjänimeä, ei löytynyt. Luodaan uusi käyttäjä.", Toast.LENGTH_SHORT).show();

        return generated != null;
    }


    //Creates SHA256 hashes with salt
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getPass(String passwordToHash, byte[] salt, String username, boolean notCreating) {
        File asset = new File(context.getFilesDir().getPath()+"database.json");
        //Copys the database from assets on first install
        if(!asset.exists()){
            try {
                copydatabase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String generatedPassword = null;
        //creates new .json
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

    //Copies database
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

    // Creates salt
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[64];
        sr.nextBytes(salt);
        return salt;
    }

    //Returns true if user is found with correct password
    //This should be on a server but we dont have resources for that.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean validatePassword(String givenPass, String username) throws NoSuchAlgorithmException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            JSONArray array = (JSONArray) parser.parse(new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"database.json")));
            for(int i = 0; i < array.size(); i++){
                jsonObject = (JSONObject) array.get(i);
                String name = (String) jsonObject.get("username");
                if(name.equals(username)){
                    String hash = (String) jsonObject.get("hash");
                    String salt = (String) jsonObject.get("salt");

                    byte[] salt2 = Base64.getDecoder().decode(salt);
                    String b = getPass(givenPass, salt2, "", true);

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
                if(name.equals(username)){
                    System.out.print("Username found\n");
                    return true;
                }
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Creates a new file for userdata. This should be encrypted too.
    ////This should be on a server but we dont have resources for that.
    public void loadUserData(String username){
        File asset = new File(context.getFilesDir().getPath()+"userdata"+username+".json");
        //creates copy of userdata asset
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
    }

    //Loads the data from json to be manipulated for the userpage
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
                movieList.put(movieObject.get("id").toString(), new Movie(movieObject.get("name").toString(), Integer.parseInt(movieObject.get("id").toString()), movieObject.get("description").toString(), "", null, 0, Integer.parseInt(movieObject.get("year").toString()), "", Integer.parseInt(movieObject.get("personal").toString())));

            }
            return movieList;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Saves a movie from the movielist
    public static void saveMovie(USER user, Movie movie){

        JSONObject obj = new JSONObject();
        try{
            JSONParser parser = new JSONParser();

            BufferedReader br = new BufferedReader (new InputStreamReader(new FileInputStream(context.getFilesDir().getPath()+"userdata"+user.getUsername()+".json")));
            /*String line;
            while((line = br.readLine()) != null){
                System.out.println(line);
            }*/
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

    //Saves a review for the movie = edits the json
    public void saveReview(USER user, Movie movie, String moviename, int review){
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

    //Class for tracking the user
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
