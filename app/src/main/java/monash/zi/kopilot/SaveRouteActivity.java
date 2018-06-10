package monash.zi.kopilot;

import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SaveRouteActivity extends AppCompatActivity {

    TextView routeTitleEditText;
    TextView routeCreatorEditText;
    TextView routeDescriptionEditText;
    TextView routePlanetToPlanetTextView;
    Button saveAndUploadButton;
    ImageView startPlanet;
    ImageView destPlanet;

    // Request code we will be checking for
    private static final int LOCATION_REQUEST_CODE = 1337;

    // Variables required for permissions
    private boolean canAccessLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_route);

        canAccessLocation = (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        // If we do not have permissions then request them
        if (!canAccessLocation) {
            requestPermissions();
        }

        // Route details from previous intent
        final ArrayList<String> routeToSave = getIntent().getStringArrayListExtra("planetsInRoute");

        routeTitleEditText = findViewById(R.id.routeTitleEditText);
        routeCreatorEditText = findViewById(R.id.routeAuthorEditText);
        routeDescriptionEditText = findViewById(R.id.routeDescEditText);
        routePlanetToPlanetTextView = findViewById(R.id.planetToPlanetTextView);
        saveAndUploadButton = findViewById(R.id.routeSaveAndUploadButton);
        startPlanet = findViewById(R.id.routeStartPlanetImageView);
        destPlanet = findViewById(R.id.routeDestPlanetImageView);

        // Setup details from intent
        routePlanetToPlanetTextView.setText(String.format("Route from %s to %s", routeToSave.get(0), routeToSave.get(1)));
        setupImages(routeToSave.get(0), routeToSave.get(1));

        // set listeners
        // Button: Save/Upload route details to firebase.
        saveAndUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditTexts()) {
                    // Set refs to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Routes");

                    // Construct JSON map to upload to firebase
                    Map locDetailMap = new HashMap();
                    locDetailMap.put("routeName", routeTitleEditText.getText());
                    locDetailMap.put("routeDescription", routeDescriptionEditText.getText());
                    locDetailMap.put("startPlanet", routeToSave.get(0));
                    locDetailMap.put("destPlanet", routeToSave.get(1));

                    Map locDetailMetaDataMap = new HashMap();
                    locDetailMetaDataMap.put("creator", routeCreatorEditText.getText());

                    Map locDetailCreateLocation = new HashMap();
                    if (canAccessLocation) {
                        locDetailCreateLocation.put("latitude", locationServicesGetLat);
                        locDetailCreateLocation.put("longitude", locationServicesGetLong);
                    }
                    else {
                        // Default to melbourne CBD as the source of the route if permissions not allowed
                        Toast.makeText(getApplicationContext(), "No locations permissions granted, defaulting location.", Toast.LENGTH_SHORT).show();
                        locDetailCreateLocation.put("latitude", "-37.814");
                        locDetailCreateLocation.put("longitude", "144.9633");
                    }

                    locDetailMap.put("metaData", locDetailMetaDataMap);
                    locDetailMetaDataMap.put("createLocation", locDetailCreateLocation);

                    // set value on Firebase
                    myRef.child((String) locDetailMap.get("routeName")).setValue(locDetailMap);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Missing input", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Save the details of the route from text views into the parceleable route object. (May not be need for upload)
        // Upload to firebase:
        //  Route details
        //  Location details
        // Return to create/view screen when done.
    }

    public void requestPermissions() {
        // Check if we need to provide information to the user
        // We are checking if we need permission for fine location
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Show the user an explanation on why we need the permission and then ask again
        } else {
            // We do not need to show the user info we can just request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }

    }

    private boolean validateEditTexts() {
        return !(routeTitleEditText.getText() == null || routeCreatorEditText.getText() == null || routeDescriptionEditText.getText() == null);
    }

    private void setupImages(String startPlanetName, String destPlanetName) {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://kopilot-database.appspot.com");

        // Create a reference with an initial file path and name
        StorageReference startPlanetImgRef = storageReference.child(String.format("/%s.png", startPlanetName.toLowerCase()));

        final long ONE_MB = 1024 * 1024;
        startPlanetImgRef.getBytes(ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                startPlanet.setImageBitmap(BitmapFactory
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

        // Create a reference with an initial file path and name
        StorageReference destPlanetImgRef = storageReference.child(String.format("/%s.png", destPlanetName.toLowerCase()));

        destPlanetImgRef.getBytes(ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                destPlanet.setImageBitmap(BitmapFactory
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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // Check with request code has been given to us
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // This is a location permission request so lets handle it
                if (grantResults.length > 0) {
                    // Can access coarse is equal to
                    canAccessLocation = (grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                break;
        }
        // If at this point we have permissions for location attempt to start it
        if (canAccessLocation) {
            // We can now access our location services
        }
        else {
            // Display error saying we cannot start location service without permission
            Toast.makeText(this, "Locations will not be displayed without permissions",
                    Toast.LENGTH_LONG).show();
        }
    }
}
