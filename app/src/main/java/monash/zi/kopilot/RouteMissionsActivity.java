package monash.zi.kopilot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RouteMissionsActivity extends AppCompatActivity {

    Button createRoute;
    Button userMissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_missions);
        setTitle("Routes and Missions");

        // init
        createRoute = findViewById(R.id.buttonCreateRoute);
        userMissions = findViewById(R.id.buttonUserMissions);

        // set listeners
        // Button: Start creating a route
        createRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RouteMissionsActivity.this, CreateRouteStartActivity.class));
            }
        });

        // Button: User Missions
        userMissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RouteMissionsActivity.this, RouteUserMissionsActivity.class));
            }
        });
    }
}
