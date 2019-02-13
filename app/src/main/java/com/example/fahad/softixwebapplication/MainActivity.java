package com.example.fahad.softixwebapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private WebView webView ;
    private static final String SHARED_PREF = "sharedPrefs" ;
    private static final String TEXT = "text" ;
    private String text ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String data = getIntent().getStringExtra("ID") ;
        webView = findViewById(R.id.webview);

            if (data.equals("") && data.isEmpty()) {
                Toast.makeText(this, "User ID is empty", Toast.LENGTH_LONG).show();
            }

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
            text = sharedPreferences.getString(TEXT, "https://erplive.net");

            if (!text.isEmpty() && !text.equals("")) {
                if (!text.contains("/ebiz/" + data+ "/") || !text.contains("ebiz/" + data + "/")) {
                    char lastChar = text.charAt(text.length() - 1);
                    if (isOnline()) {
                        if (lastChar != '/') {
                            webView.loadUrl(text + "/ebiz/" + data + "/");
                            WebSettings webSettings = webView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                        } else if (lastChar == '/') {
                            webView.loadUrl(text + "ebiz/" + data + "/");
                            WebSettings webSettings = webView.getSettings();
                            webSettings.setJavaScriptEnabled(true);
                            webView.setWebViewClient(new WebViewClient());
                        } else {
                            Toast.makeText(MainActivity.this, "Your server input is not correct !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Server Input is not correct", Toast.LENGTH_SHORT).show();
            }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_exit) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this) ;
            alertdialog.setMessage("Are you sure you want to exit ?") ;
            alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertdialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertdialog.show();
        } else if (id == R.id.nav_server) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(this) ;
            alertdialog.setTitle("Server :") ;
            final EditText serverInputs = new EditText(this) ;
            serverInputs.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp1.setMargins(30, 20, 30, 30);

            Resources r = getResources();
            int px = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());

            serverInputs.setWidth(px);
            layout.addView(serverInputs, lp1);

            alertdialog.setView(layout);

            final String dataID = getIntent().getStringExtra("ID") ;
            SharedPreferences sharedP = getSharedPreferences(SHARED_PREF, MODE_PRIVATE) ;
            text = sharedP.getString(TEXT, "https://erplive.net");

                serverInputs.setText(text);

            alertdialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(serverInputs!=null){
                        String server = serverInputs.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE) ;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TEXT, server) ;
                        editor.apply();
                        if (!server.contains("/ebiz/" + dataID + "/") || !server.contains("ebiz/" + dataID + "/")) {
                            char lastChar = server.charAt(server.length() - 1);
                            if (lastChar != '/') {
                                webView.loadUrl(server + "/ebiz/" + dataID + "/");
                                WebSettings webSettings = webView.getSettings() ;
                                webSettings.setJavaScriptEnabled(true);
                                webView.setWebViewClient(new WebViewClient());
                            } else if (lastChar == '/') {
                                webView.loadUrl(server + "ebiz/" + dataID + "/");
                                WebSettings webSettings = webView.getSettings() ;
                                webSettings.setJavaScriptEnabled(true);
                                webView.setWebViewClient(new WebViewClient());
                            } else {
                                Toast.makeText(MainActivity.this, "Your server input is not correct !", Toast.LENGTH_SHORT).show();
                            }
                        }
                        WebSettings webSettings = webView.getSettings() ;
                        webSettings.setJavaScriptEnabled(true);
                        webView.setWebViewClient(new WebViewClient());

                        Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(MainActivity.this, "Enter server input",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = alertdialog.create() ;
            alert.show() ;
            Button bq = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bq.setTextColor(getResources().getColor(R.color.colorNavBack));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            bp.setTypeface(boldTypeface);
            bp.setTextColor(getResources().getColor(R.color.colorNavBack));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (keyCode){
                case KeyEvent.KEYCODE_BACK:
                if (webView.canGoBack()){
                    webView.goBack();
                }else {finish();
                }
                return true ;
            }
        }
        return super.onKeyDown(keyCode,event) ;
    }
    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            final AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setTitle("No internet found !");
            alertbox.setMessage("Check your network connection !");
            alertbox.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            alertbox.show();
            return false;
        }
        return true;
    }
}