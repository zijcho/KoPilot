package monash.zi.kopilot;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class PlanetInfoActivity extends AppCompatActivity {
    TextView planetNameTextView;
    TextView planetDataTextView;
    TextView planetDescriptionTextView;
    ImageView planetInfoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet_info);

        // setup ui elements
        planetNameTextView = findViewById(R.id.planetNameTextView);
        planetDataTextView = findViewById(R.id.planetDataTextView);
        planetDescriptionTextView = findViewById(R.id.planetDescriptionTextView);
        planetInfoImageView = findViewById(R.id.planetInfoImageView);


        // assuming we get an intent when arriving to this screen.
        final String selectedPlanet = getIntent().getStringExtra("planetToView");
        assert selectedPlanet != null;

        String planetarySystemReference = retrievePlanetSystem(selectedPlanet);

        // Set refs to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Planets");
        DatabaseReference planetRef = myRef.child(planetarySystemReference).child(selectedPlanet);

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://kopilot-database.appspot.com");

        // Create a reference with an initial file path and name
        StorageReference planetImageReference = storageReference.child(String.format("/%s.png", selectedPlanet.toLowerCase()));

        // Load the planet image
        final long ONE_MB = 1024 * 1024;
        planetImageReference.getBytes(ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                planetInfoImageView.setImageBitmap(BitmapFactory
                        .decodeByteArray(bytes, 0, bytes.length));
                System.out.println("Success in image download");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("Error in image download");
            }
        });

        // Read from the database - load planet data
        planetRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Constant real-time changes would be rare however.
                HashMap value = (HashMap) dataSnapshot.getValue();

                // set values of text views.
                planetNameTextView.setText(selectedPlanet);
                planetDataTextView.setText(String.format(
                        "Area: %s (m^2)\n" +
                        "Equator Radius: %s (m)\n" +
                        "Escape Velocity: %s (m/s)\n" +
                        "Gravitational Parameter: %s\n" +
                        "Gravity: %s\n" +
                        "Mass: %s (kg)\n" +
                        "Rotation Period: %s (s)\n" +
                        "Sphere of influence: %s (m)",
                        value.get("Area"),
                        value.get("Equator Radius"),
                        value.get("Escape Velocity"),
                        value.get("GM"),
                        value.get("Gravity"),
                        value.get("Mass"),
                        value.get("Rotation Period"),
                        value.get("SOI")));

                planetDescriptionTextView.setText(String.format(
                        "Description:\n" +
                                "%s",
                        value.get("Description")
                ));

                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }

    // Input a planet name, function will return it's planetary system that the planet resides in.
    private String retrievePlanetSystem(String inputPlanetName) {
        String[] planetarySystems = {"Dres System", "Duna System", "Eeloo System", "Eve System", "Jool System", "Kerbin System", "Moho System"};

        switch (inputPlanetName) {
            case "Dres":
                return planetarySystems[0];
            case "Duna":
            case "Ike":
                return planetarySystems[1];
            case "Eeeloo":
                return planetarySystems[2];
            case "Eve":
            case "Gilly":
                return planetarySystems[3];
            case "Jool":
            case "Bop":
            case "Laythe":
            case "Pol":
            case "Tylo":
            case "Vall":
                return planetarySystems[4];
            case "Kerbin":
            case "Mun":
            case "Minmus":
                return planetarySystems[5];
            case "Moho":
                return planetarySystems[6];
            default:
                return "ERROR: Input planet name is invalid";
        }
    }

}
