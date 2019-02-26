package com.user.alltronics.ruralpro;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilterProblems extends AppCompatActivity implements View.OnClickListener{

    private TextView TvDistrict;
    private TextView TvMandal;
    private TextView TVProblem;

    private CardView buttonDistrict;
    private CardView buttonMandal;
    private CardView buttonProblemType;

    private Spinner language;

    String TvDistrictT, TvMandalT, TVProblemT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_problems);

        TvDistrict = (TextView) findViewById (R.id.TvDistrict);
        TvMandal = (TextView) findViewById (R.id.TvMandal);
        TVProblem = (TextView) findViewById (R.id.TVProblem);


        buttonDistrict = (CardView) findViewById (R.id.buttonDistrict);
        buttonMandal = (CardView) findViewById (R.id.buttonMandal);
        buttonProblemType = (CardView) findViewById (R.id.buttonProblemType);

        TvDistrictT= TvDistrict.getText().toString();
        TvMandalT= TvMandal.getText().toString();
        TVProblemT= TVProblem.getText().toString();

        language = (Spinner) findViewById(R.id.language);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("English");
        spinnerArray.add("Telugu");
        spinnerArray.add("Hindi");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);

        language.setSelection(LanguageId.language_id);

        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.e("Position", Integer.toString(position));


                if ( position == 0 ) {

                    LanguageId.language_id = 0;


                    TvDistrict.setText(TvDistrictT);
                    TvMandal.setText(TvMandalT);
                    TVProblem.setText(TVProblemT);

                }

                if ( position == 1 ) {

                    LanguageId.language_id = 1;
                    TvDistrict.setText("జిల్లా వారీగా సమస్యలు");
                    TvMandal.setText("మండలం వారీగా సమస్యలు");
                    TVProblem.setText("సమస్య రకాలు వారీగా సమస్యలు");

                }

                if ( position == 2 ) {

                    LanguageId.language_id = 2;

                    TvDistrict.setText("जिला से खोजें");
                    TvMandal.setText("क्षेत्र से खोजें");
                    TVProblem.setText("समस्या प्रकार से खोजें");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonDistrict.setOnClickListener(this);
        buttonMandal.setOnClickListener(this);
        buttonProblemType.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == buttonDistrict ) {
            startActivity(new Intent(this,FilterByDistrict.class));

        }if (v == buttonMandal ) {
            startActivity(new Intent(this,FilterByMandal.class));

        }if (v == buttonProblemType ) {
            startActivity(new Intent(this,FilterByProblemType.class));

        }

    }
}
