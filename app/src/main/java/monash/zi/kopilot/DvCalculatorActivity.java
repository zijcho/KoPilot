package monash.zi.kopilot;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DvCalculatorActivity extends AppCompatActivity implements View.OnClickListener{
    //Todo: Explore an alternative to include different engine types (see wireframe document). Currently only supporting a single stage rocket.

    TextView totalMass;
    TextView emptyMass;
    TextView engineISP;
    TextView resultDV;

    Button calculateDV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dv_calculator);

        totalMass = findViewById(R.id.totalMassInputEditText);
        emptyMass = findViewById(R.id.emptyMassInputEditText);
        engineISP = findViewById(R.id.engineISPInputeditText);
        resultDV = findViewById(R.id.totalDVtextView);
        calculateDV = findViewById(R.id.calculateDVbutton);

        calculateDV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                try {
                    Long craftTotalMass = Long.parseLong(totalMass.getText().toString());
                    Long craftEmptyMass = Long.parseLong(emptyMass.getText().toString());
                    Long craftEngineISP = Long.parseLong(engineISP.getText().toString());

                    if (craftEmptyMass >= craftTotalMass) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Empty mass cannot be equal or greater to the start mass", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else {
                        double result = craftEngineISP * Math.log(craftTotalMass / craftEmptyMass);
                        resultDV.setText(String.format("Total ∆v: %.2f m/s", result));
                    }

                } catch (Exception e) {
                    System.out.println("Error in trying to calculate DV.");
                }
            }
        });
    }

    @Override
    public void onClick (View view) {

    }
}
