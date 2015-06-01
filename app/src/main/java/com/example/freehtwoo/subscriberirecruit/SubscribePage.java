package com.example.freehtwoo.subscriberirecruit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.freehtwoo.subscriberirecruit.MainActivity;
import com.example.freehtwoo.subscriberirecruit.R;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class SubscribePage extends ActionBarActivity {

    public final static String Message_KEY="unquie_meessage_key";
    private EditText firstnameET;
    private EditText lastnameET;
    private EditText emailET;
    private TextView messageTV;
    private Button submitButton;

    private Spinner spnr;
    private int language=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_page);
        Intent intent=getIntent();
        if(intent!=null) {
            language = intent.getIntExtra(Message_KEY,0);
        }
        setUpSniper(language);
    }

    //Sets up the dropdown menu contents for language selection and the text language in the GUI
    public void setUpSniper(int selectedLanguage) {
        String[] languages = {
                "English",
                "Dansk",
        };

        final String[] messages = {
                "Please fill in the following fields before submitting",
                "Udfyld venligst nedenstende felter , for du sender",
        };

        final String[] firstnameList = {
                "First Name",
                "Fornavn",
        };

        final String[] lastnameList = {
                "Last Name",
                "Efternavn",
        };

        final String[] buttonText = {
                "Submit",
                "Indsend",
        };

        spnr = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, languages);

        spnr.setAdapter(adapter);
        spnr.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        language = spnr.getSelectedItemPosition();
                        firstnameET = (EditText) findViewById(R.id.firstnameTextView);
                        firstnameET.setHint(firstnameList[language]);
                        lastnameET = (EditText) findViewById(R.id.lastnamenameTextView);
                        lastnameET.setHint(lastnameList[language]);
                        messageTV = (TextView) findViewById(R.id.messageTV);
                        messageTV.setText(messages[language]);
                        submitButton = (Button) findViewById(R.id.submitButton);
                        submitButton.setText(buttonText[language]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );
        spnr.setSelection(selectedLanguage);
    }

    //Return to the MainActivity when click on logo
    public void navigateHome(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Message_KEY, language);
        startActivity(intent);
    }

    //Subscribe button on click method
    public void onClick(View v){
        String[] emailList = {
                "Invalid email",
                "Ugyldig email",
        };
        String[] firstnameList = {
                "No firstname",
                "Ingen fornavn",
        };
        String[] lastnameList = {
                "No lastname",
                "Ingen efternavn",
        };
        String[] submitList = {
                "Submitting...",
                "Indsendelse...",
        };

        firstnameET=(EditText) findViewById(R.id.firstnameTextView);
        lastnameET=(EditText) findViewById(R.id.lastnamenameTextView);
        emailET=(EditText) findViewById(R.id.emailTextView);

        String firstname=firstnameET.getText().toString();
        String lastname=lastnameET.getText().toString();
        String email=emailET.getText().toString();

        //checking if all the fields are valid, if not Toast appropriate message
        if(checkEmail(email)==false) {
            Toast.makeText(this,emailList[language],Toast.LENGTH_SHORT).show();
        }
        else if(firstname.length()==0) {
            Toast.makeText(this,firstnameList[language],Toast.LENGTH_SHORT).show();
        }
        else if(lastname.length()==0) {
            Toast.makeText(this,lastnameList[language],Toast.LENGTH_SHORT).show();
        }
        else { //if everything is alright, call the execute method on the Networking (extends Async) class that makes the POST
            Toast.makeText(this,submitList[language],Toast.LENGTH_SHORT).show();
            new Networking(firstname, lastname, email, SubscribePage.this, language).execute();
        }
    }

    //Regex check for valid email
    private boolean checkEmail(String email) {
        String Expn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        if (email.matches(Expn) && email.length() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
