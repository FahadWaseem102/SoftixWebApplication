package com.example.fahad.softixwebapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseLongArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    ImageView imgView ;
    Button buttonGetStarted ;
    Animation animatiomFrombottom, animationFromtop ;
    private static final String SHARED_PREFS = "sharedPref" ;
    private static final String TEXT = "text" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        buttonGetStarted = findViewById(R.id.button) ;
        imgView = findViewById(R.id.imageView) ;
        animatiomFrombottom = AnimationUtils.loadAnimation(this, R.anim.splash_anim) ;
        animationFromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop) ;
        buttonGetStarted.setAnimation(animatiomFrombottom);
        imgView.setAnimation(animationFromtop);

        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                boolean firstStart = prefs.getBoolean("firstStart", true);
                if (firstStart){
                    showDialogue();
                } if(firstStart==false){
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    String data = sharedPreferences.getString(TEXT, null);
                    Intent intentToMain = new Intent(SplashActivity.this, MainActivity.class);
                    intentToMain.putExtra("ID", data);
                    startActivity(intentToMain);
                    SplashActivity.this.finish();
                }
            }
        });
    }
    public void showDialogue(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this) ;
        alertDialog.setTitle("Enter Application ID :") ;
        alertDialog.setIcon(R.drawable.ic_action_account_id) ;
        final EditText input = new EditText(SplashActivity.this) ;
        LinearLayout layout = new LinearLayout(SplashActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        input.setHint("ID");
        input.setHintTextColor(getResources().getColor(R.color.colorButtonToNav));
        input.setPadding(25,0,0,25);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp1.setMargins(30, 30, 30, 30);

        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());

        input.setWidth(px);
        layout.addView(input, lp1);

        alertDialog.setView(layout);
        alertDialog.setPositiveButton("GO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = input.getText().toString();
                if (!id.equals("")){
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TEXT, id);
                editor.apply();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
                SplashActivity.this.finish();}else {
                    Toast.makeText(SplashActivity.this, "Enter the application ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final AlertDialog alert = alertDialog.create();
        alert.show();
        Button bq = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        bq.setTextSize(18);
        bq.setTextColor(getResources().getColor(R.color.colorNavBack));
        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
        bq.setTypeface(boldTypeface);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}
