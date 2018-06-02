package monash.zi.kopilot;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SelectPlanetFragment extends Fragment {
    private String planetNameText;

    private TextView planetName;
    private ImageView planetImage;

    public SelectPlanetFragment() {
        // Required empty public constructor
    }

    public static SelectPlanetFragment newInstance(String inPlanetName) {
        SelectPlanetFragment fragment = new SelectPlanetFragment();


        Bundle args = new Bundle();
        args.putString("inPlanetName", inPlanetName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planetNameText = getArguments().getString("inPlanetName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_select_planet, container);

        planetName = (TextView) view.findViewById(R.id.FragTextView);
        planetImage = (ImageView) view.findViewById(R.id.FragImageView);

        planetName.setText(planetNameText);
        loadDefaultDataFirebase(planetNameText);

        return inflater.inflate(R.layout.fragment_select_planet, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadDefaultDataFirebase(String planetToLoadString) {
        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl("gs://kopilot-database.appspot.com");

        // Create a reference with an initial file path and name
        StorageReference planetImageReference = storageReference.child(String.format("/%s.png", planetToLoadString.toLowerCase()));

        final long ONE_MB = 1024 * 1024;
        planetImageReference.getBytes(ONE_MB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                planetImage.setImageBitmap(BitmapFactory
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
}
