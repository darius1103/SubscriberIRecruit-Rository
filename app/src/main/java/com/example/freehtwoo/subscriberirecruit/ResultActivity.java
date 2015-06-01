package com.example.freehtwoo.subscriberirecruit;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class ResultActivity extends ActionBarActivity {

    public final static String Message_KEY="unquie_meessage_key";
    public final static String Message_KEY2 = "unquie_meessage_key_result";
    private TextView messageTV;
    private Button submitButton;
    private String result;
    private Spinner spnr;
    private int language=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent=getIntent();
        if(intent!=null) {
            language = intent.getIntExtra(Message_KEY,0);
            result= intent.getStringExtra(Message_KEY2);
        }
        setUpSniper(language, result);
    }

    //Return to the MainActivity when click on logo
    public void navigateHome(View v){
        Intent intent= new Intent(this,MainActivity.class);
        intent.putExtra(Message_KEY, language);
        startActivity(intent);
    }

    //Sets up the dropdown menu contents for language selection and the text language in the GUI
    public void setUpSniper(int selectedLanguage, final String result) {
        String[] languages = {
                "English",
                "Dansk",
        };

        final String[] messages = {
                "Thank you for subscribing. An email has been send to the provided address with further information.",
                "Tak for din tilmelding. En e-mail er sendt til den angivne adresse med yderligere oplysninger.",
                "The mail you provided is already in use.",
                "Mailen du angav er allerede i brug.",
                "Connection could not be established.",
                "Connection kan ikke etableret.",
                "An error has occur and no connection was established, please try again later.",
                "En fejl har forekomme, og ingen forbindelse blev etableret , prov venligst igen senere."
        };

        final String[] buttonText = {
                "Return",
                "Retur",
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
                        messageTV = (TextView) findViewById(R.id.messageTV);
                        if(result.equals("worked"))
                            messageTV.setText(messages[language]);
                        else if(result.equals("email in use"))
                            messageTV.setText(messages[language+2]);
                        else if(result.equals("timeout"))
                            messageTV.setText(messages[language+4]);
                        else
                            messageTV.setText(messages[language+6]);
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

    //Returns to the MainActivity if everything was alright, or the Subscribe Activity if something went wrong
    public void onClick(View v) {
        if(result.equals("worked")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Message_KEY, language);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, SubscribePage.class);
            intent.putExtra(Message_KEY, language);
            startActivity(intent);
        }
    }

}
