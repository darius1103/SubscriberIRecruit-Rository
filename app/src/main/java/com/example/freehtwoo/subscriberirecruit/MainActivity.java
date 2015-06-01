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
import android.widget.Toast;

import com.example.freehtwoo.subscriberirecruit.R;


public class MainActivity extends ActionBarActivity {

    public final static String Message_KEY="unquie_meessage_key";
    public final static String Message_KEY2 = "unquie_meessage_key_result";
    private TextView messageTV;
    private Button subscibeButton;
    private Spinner spnr;
    private int language=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                "Wellcome to i-Recruit! i-Recruit is the first Patient Recruitment Service Provider that offers a full-service solution in Intelligent Patient Recruitment.",
                "Velkommen til i-Recruit ! i-Recruit er den forste udbyder af patientrekrutteringsservice which offers en komplet solution to the intelligent patientrekruttering.",
        };


        final String[] buttonText = {
                "Subscribe",
                "Tilmeld",
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
                        messageTV.setText(messages[language]);
                        subscibeButton = (Button) findViewById(R.id.submitButton);
                        subscibeButton.setText(buttonText[language]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }

                }
        );
        spnr.setSelection(selectedLanguage);
    }

    //Forwards to the SubscribePage when click
    public void onClick(View v) {
        Intent intent = new Intent(this, com.example.freehtwoo.subscriberirecruit.SubscribePage.class);
        //attaching the currently selected language
        intent.putExtra(Message_KEY, language);
        startActivity(intent);
    }
}
