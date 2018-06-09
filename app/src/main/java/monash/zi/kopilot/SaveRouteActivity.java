package monash.zi.kopilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class SaveRouteActivity extends AppCompatActivity {

    TextView routeTitleTextView;
    TextView routeDescriptionTextView;
    TextView routeCreatorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);

        // Route details from previous intent
        final ArrayList<String> routeToSave = getIntent().getStringArrayListExtra("planetsInRoute");

        // Save the details of the route from text views into the parceleable route object.
        // Upload to firebase:
        //  Route details
        //  Location details
        // Return to create/view screen when done.

    }
}
