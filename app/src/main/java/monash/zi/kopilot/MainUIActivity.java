package monash.zi.kopilot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

// We should check for internet functionality here, to then enable routes or not, based on caching.
// On first starting, the app should initialize at the very least, if the app for some reason was
// started without an internet connection, then we'll warn the user that 2/4 of the functionality
// will be unavailable (routes, and sharing).

public class MainUIActivity extends AppCompatActivity {

    Button routesMissions;
    Button calculateDV;
    Button checklists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);

        // init
        routesMissions = findViewById(R.id.buttonRoutesMissions);
        calculateDV = findViewById(R.id.buttonCalculateDv);
        checklists = findViewById(R.id.buttonChecklists);

        // set listeners
        // Button: Routes and user missions
        routesMissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainUIActivity.this, RouteMissionsActivity.class));
            }
        });

        // Button: Delta V calculator
        calculateDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainUIActivity.this, DvCalculatorActivity.class));
            }
        });

        // Button: Pre-launch checklists
        checklists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainUIActivity.this, ChecklistListViewActivity.class));
            }
        });
    }
}
