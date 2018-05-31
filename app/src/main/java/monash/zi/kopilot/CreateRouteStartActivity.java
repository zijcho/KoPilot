package monash.zi.kopilot;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
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

public class CreateRouteStartActivity extends AppCompatActivity {
    // for the purposes of the demo, I'll hard code in the start and end points, but it is important to note
    // that since we're demonstrating the fire-base interaction, it's stressed that little to no data of the planets
    // themselves are stored locally.

    TextView planetName;
    ImageView planetImage;
    Button planetSelectButton;

    String tempPlanetSelection = "Kerbin";
    ArrayList<String> planetList;
    Route newRoute;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_start);
        setTitle("Select a start point:");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)
                findViewById(R.id.character_view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // set listeners
        // Button: Routes and user missions
        planetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteStartActivity.this, PlanetInfoActivity.class);
//                newIntent.putExtra("planetToView", tempPlanetSelection);

                startActivity(newIntent);
            }
        });

        // Button: Select destination
        planetSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(CreateRouteStartActivity.this, CreateRouteDestActivity.class);
//                newIntent.putExtra("planetStartPoint", tempPlanetSelection);
                startActivity(newIntent);
            }
        });
    }

    private void setupPlanetList() {
        // Todo: loop/get data (planet names) from firebase instead
        planetList.add("Kerbin");
        planetList.add("Mun");
        planetList.add("Minmus");


    }

    private void loadDefaultDataFirebase(String planetToLoadString) {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://kopilot-database.appspot.com");

        // Create a reference with an initial file path and name
        StorageReference planetImageReference = storageReference.child(String.format("/%s.png", planetToLoadString.toLowerCase())); //todo: hardcoded value here (used for demo)

        final long ONE_MB = 1024 * 1024;
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
