package com.thwrayman.matchar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {
    String aboutMeText = "Please enter something about yourself!";

    private static final int SELECT_PHOTO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUserDetails();
        final ActionBar actionBar = getActionBar();

        getActionBar().setLogo(R.drawable.logo60);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       setTheme(android.R.style.Theme);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ActionBar actionBar = getActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.action_camera_view){
            Intent cameraIntent = new Intent(this,GoToCam.class);
            startActivity(cameraIntent);
        }

        else if (id == R.id.action_post_message){

        }

        return super.onOptionsItemSelected(item);
    }





    public void setUserDetails(){

        runOnUiThread(new Runnable() {
            @Override

            public void run() {

                SignUp userdet = new SignUp();
                UserDetails currentUser = userdet.NewUser;

                TextView name = (TextView)findViewById(R.id.about_name_text_view);
                String fname = currentUser.getFname();
                String capfname = fname.substring(0,1).toUpperCase() + fname.substring(1);

                String lname = currentUser.getLname();
                String caplname = lname.substring(0,1).toUpperCase()+lname.substring(1);

                name.setText(capfname + " " + caplname);

                TextView about = (TextView)findViewById(R.id.about_text_view);
                String job = currentUser.getJob();
                String capjob = job.substring(0,1).toUpperCase() + job.substring(1);
                about.setText(currentUser.getAge()+", "+capjob);
            }

            });





    }



    public void aboutMeTVClicked(View view) {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        runOnUiThread(new Runnable() {
            @Override

            public void run() {


                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                TextView amtv = (TextView) findViewById(R.id.about_me_text_view);
                amtv.setVisibility(View.GONE);

                TextView amet = (TextView) findViewById(R.id.about_me_edit_text);
                amet.setVisibility(View.VISIBLE);

                Button savebut = (Button) findViewById(R.id.save_button);
                savebut.setVisibility(View.VISIBLE);

                Button editbut = (Button) findViewById(R.id.edit_button);
                editbut.setVisibility(View.GONE);
            }
        });
    }



    public void saveTextClick(View view) {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);

        runOnUiThread(new Runnable() {
            @Override

            public void run() {
                EditText amet = (EditText) findViewById(R.id.about_me_edit_text);
                aboutMeText = amet.getText().toString();
                amet.setVisibility(View.GONE);

                TextView amtv = (TextView) findViewById(R.id.about_me_text_view);
                amtv.setVisibility(View.VISIBLE);
                amtv.setText(aboutMeText);

                Button savebut = (Button) findViewById(R.id.save_button);
                savebut.setVisibility(View.GONE);

                Button editbut = (Button) findViewById(R.id.edit_button);
                editbut.setVisibility(View.VISIBLE);

                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

        });
    }

    public void checkApp(){
        final String PREFS_NAME = "MyPrefsFile";
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");


            Intent SingUp = new Intent(this,SignUp.class);
            startActivity(SingUp);


            Toast.makeText(getApplicationContext(), "this is my Toast message!!! =)",
                    Toast.LENGTH_LONG).show();
            // first time task

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
        else{
            Intent profile = new Intent(this,MainActivity.class);
            startActivity(profile);
        }
    }




    public void ChangePic(View view) {


        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        runOnUiThread(new Runnable() {
            @Override

            public void run() {
                ImageView profPic = (ImageView)findViewById(R.id.photo_main);

                switch(requestCode) {
                    case SELECT_PHOTO:
                        if(resultCode == RESULT_OK){
                            Uri selectedImage = imageReturnedIntent.getData();

                            String[] filePathColumn = {MediaStore.Images.Media.DATA};

                            Cursor cursor = getContentResolver().query(
                                    selectedImage, filePathColumn, null, null, null);
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String filePath = cursor.getString(columnIndex);
                            cursor.close();


                            profPic.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        }
                }
            }

            });






    }
}
