package monash.zi.kopilot;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveRouteActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    TextView routeTitleEditText;
    TextView routeCreatorEditText;
    TextView routeDescriptionEditText;
    TextView routePlanetToPlanetTextView;
    Button saveAndUploadButton;
    ImageView startPlanet;
    ImageView destPlanet;

    // Min and Max Update Intervals for our Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000; // 2 Seconds

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
                    Map routeMap = new HashMap();
                    routeMap.put("routeName", routeTitleEditText.getText().toString());
                    routeMap.put("routeDescription", routeDescriptionEditText.getText().toString());
                    routeMap.put("startPlanet", routeToSave.get(0));
                    routeMap.put("destPlanet", routeToSave.get(1));

                    Map routeMetaDataMap = new HashMap();
                    routeMetaDataMap.put("creator", routeCreatorEditText.getText().toString());

                    Map routeCreateLocation = new HashMap();
                    if (canAccessLocation) {
                        routeCreateLocation.put("latitude", String.valueOf(getGPS()[0]));
                        routeCreateLocation.put("longitude", String.valueOf(getGPS()[1]));
                    } else {
                        // Default to melbourne CBD as the source of the route if permissions not allowed
                        Toast.makeText(getApplicationContext(), "No locations permissions granted, defaulting location.", Toast.LENGTH_SHORT).show();
                        routeCreateLocation.put("latitude", "-37.814");
                        routeCreateLocation.put("longitude", "144.9633");
                    }

                    routeMap.put("metaData", routeMetaDataMap);
                    routeMetaDataMap.put("createLocation", routeCreateLocation);

                    // set value on Firebase
                    String refChildStr = String.valueOf(routeMap.get("routeName"));
                    myRef.child(refChildStr).setValue(routeMap);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Missing input", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private double[] getGPS() {
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locManager.getProviders(true);

        Location location = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                location = locManager.getLastKnownLocation(providers.get(i));
                if (location != null) break;
            }
        }

        double[] gpsArr = new double[2];
        if (location != null) {
            gpsArr[0] = location.getLatitude();
            gpsArr[1] = location.getLongitude();
        }

        return gpsArr;
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
        } else {
            // Display error saying we cannot start location service without permission
            Toast.makeText(this, "Saved routes will have a default location.",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
