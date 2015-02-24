package com.thwrayman.matchar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.provider.Settings.Secure;
import java.io.OutputStreamWriter;

import static java.security.AccessController.getContext;

/**
 * Created by emmet on 01/12/2014.
 */
public class SignUp extends Activity {
    DatabaseHandler db = new DatabaseHandler(this);
    static UserDetails NewUser;
/*    private String android_id = Secure.getString(this.getContentResolver(),
            Secure.ANDROID_ID);*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

/*        Log.d("android id",android_id);*/

        final ActionBar actionBar = getActionBar();

        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));


        actionBar.setTitle("");

        BitmapDrawable background = new BitmapDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.header55));
        background.setGravity(Gravity.CENTER);
        background.setTileModeX(Shader.TileMode.CLAMP);
        actionBar.setBackgroundDrawable(background);


    }

    public void submitDetails(View view) throws FileNotFoundException {

        EditText fname = (EditText) findViewById(R.id.first_name_edit);
        String firstname = fname.getText().toString();

        EditText lname = (EditText) findViewById(R.id.last_name_edit);
        String lasttname = lname.getText().toString();

        EditText ename = (EditText) findViewById(R.id.email_box);
        String email = ename.getText().toString();

        EditText pass = (EditText) findViewById(R.id.password_edit);
        String password = pass.getText().toString();

        if(checkInputValid(firstname, lasttname) == true){
            UserDetails newUser = new UserDetails("20", firstname, lasttname, email, "student", password);


            db.addUser(newUser);

            NewUser = newUser;
            String log = "id: " + newUser.getID() + " , fname: " + newUser.getFname()
                    + " ,lname: " + newUser.getLname() + " ,email: " + newUser.get_email()
                    + " ,password: " + newUser.get_password() + " ,job: " + newUser.getJob();

            Log.d("user: ", log);


            try {
                writeToFile(firstname, 0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            writeToFile(lasttname, 1);
            writeToFile(email, 2);
            writeToFile(password, 3);

            String filecontent = readFromFile();


            Log.d("file output", filecontent);

            Intent Mainpage = new Intent(this, MainActivity.class);
            startActivity(Mainpage);

        }





    }

    public boolean checkInputValid(String fname, String lname) {
        boolean state = true;
        if (fname == null || lname == null) {
            state = false;
            new AlertDialog.Builder(this)

                    .setTitle("Entry error")
                    .setMessage("You must enter a valid name, please try again")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }

                    });

        }
        else{
            state = true;
        }
        return state;
    }


    public UserDetails newUser (UserDetails newuser){
        return newuser;

    }

    private void writeToFile(String data, int i) throws FileNotFoundException {
/*        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("userData.txt", Context.MODE_PRIVATE));
            if(i==0){
                outputStreamWriter.write("");
                Log.d("flusher","flushed");
            }
            outputStreamWriter.append(data);
            outputStreamWriter.close();




        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }*/

        String path = this.getFilesDir().getAbsolutePath();

        File file = new File(path + "/userData.txt");

        FileOutputStream stream = new FileOutputStream(file,true);

        String newline = "/n";
        //if we're writing new details, wipe any existing ones
        if(i==0){
            FileOutputStream stream1 = new FileOutputStream(file,false);
            String blank = "";
            try {
                stream1.write(blank.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            stream.write(data.getBytes());
            stream.write(newline.getBytes());
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("userData.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    public void facebookLogin(View view) {
    }




/*    protected void checkFile(){
        File userData = new File(this.getFilesDir(), userData);
        FileOutputStream outputStream;


        if (!userData.exists()) {
            //prompt for username
            try {
                userData.createNewFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            outputStream = openFileOutput(userData, Context.MODE_PRIVATE);
            outputStream.write(userData.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void updateFile(File userData){
        FileWriter fw = new FileWriter(userData.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        //suppose  EditText_UserName is your edittext where he can enter his name
        bw.write(EditText_UserName.getText().toString());
        bw.close();

    }*/


}
