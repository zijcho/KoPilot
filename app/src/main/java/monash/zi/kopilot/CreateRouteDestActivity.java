package monash.zi.kopilot;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CreateRouteDestActivity extends AppCompatActivity {

    TextView planetName;
    ImageView planetImage;
    Button planetSelectButton; //todo: Still on the fence whether to keep this UI design

    String tempPlanetSelection = "Mun"; //todo: Demo stub

    ArrayList<String> planetsInRoute = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_dest);
        setTitle("Select a Destination:");

        // init
        planetName = findViewById(R.id.destPlanetTextView);
        planetImage = findViewById(R.id.destPlanetImageView);
        planetSelectButton = findViewById(R.id.destPlanetButton);

        // start point from previous intent
        final String startingPlanet = getIntent().getStringExtra("planetStartPoint");
        assert startingPlanet != null;

        loadDefaultDataFirebase();

        // set listeners
        // Button: Routes and user missions
        planetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteDestActivity.this, PlanetInfoActivity.class);
                newIntent.putExtra("planetToView", tempPlanetSelection);
                startActivity(newIntent);
            }
        });

        // Button: Select destination, and finish route creation
        planetSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteDestActivity.this, CreateShareRouteActivity.class);

                // Whatever the selected destination planet is
                planetsInRoute.add(startingPlanet);
                planetsInRoute.add(tempPlanetSelection);

                // Pass along the route array to the final activity in the route creation
                newIntent.putStringArrayListExtra("plannedRoute", planetsInRoute);
                startActivity(newIntent);
            }
        });
    }

    private void loadDefaultDataFirebase() {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://kopilot-database.appspot.com");

        // Create a reference with an initial file path and name
        StorageReference planetImageReference = storageReference.child(String.format("/%s.png", tempPlanetSelection.toLowerCase())); //todo: hardcoded value here (used for demo)

        final long ONE_MB = 1024 * 1024; // memory allocation
        planetImageReference.getBytes(ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                planetImage.setImageBitmap(BitmapFactory
                        .decodeByteArray(bytes, 0, bytes.length));

                System.out.println("Success in image download");

                planetName.setText(tempPlanetSelection);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Error in image download");
            }
        });
    }
}
