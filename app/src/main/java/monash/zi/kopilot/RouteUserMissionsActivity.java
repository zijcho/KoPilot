package monash.zi.kopilot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RouteUserMissionsActivity extends AppCompatActivity {
    // Displays a list of user missions, show a geographic map of contributed missions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_user_missions);
    }
}
