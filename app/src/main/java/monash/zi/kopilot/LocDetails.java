package monash.zi.kopilot;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

class LocDetails implements Parcelable{
    protected Map route;
    protected Map metaData;
    protected Map createLocation;

    protected String locationName;
    protected String locationDesc;
    protected LatLng latLng;

    public LocDetails(String inLocationName, LatLng inLatLng) {
        this.locationName = inLocationName;
        this.latLng = inLatLng;
    }

    public LocDetails(Map jsonMap) {
        // Pure string map object (from json)
        this.route = jsonMap;
        this.metaData = (Map) route.get("metaData");
        this.createLocation = (Map) metaData.get("createLocation");

        this.locationName = (String) jsonMap.get("routeName");
        this.locationDesc = (String) jsonMap.get("routeDescription");
        this.latLng = new LatLng(Double.valueOf(createLocation.get("latitude").toString()), Double.valueOf(createLocation.get("longitude").toString()));
    }

    @Override
    public String toString() {
        return displayDetails();
    }

    public String displayDetails() {
        return String.format("%s\n%s", locationName, route.get("routeDescription"));
    }

    protected LocDetails(Parcel in) {
        //        locationName = in.readString();
        //        locationDesc = in.readString();
        //        latLng = in.readParcelable(LatLng.class.getClassLoader());

        route = new HashMap<String, Object>();
        in.readMap(route, Object.class.getClassLoader());
        metaData = new HashMap<String, Object>();
        in.readMap(metaData, Object.class.getClassLoader());
    }

    public static final Creator<LocDetails> CREATOR = new Creator<LocDetails>() {
        @Override
        public LocDetails createFromParcel(Parcel in) {
            return new LocDetails(in);
        }

        @Override
        public LocDetails[] newArray(int size) {
            return new LocDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //        dest.writeString(locationName);
        //        dest.writeString(locationDesc);
        //        dest.writeParcelable(latLng, flags);
        dest.writeMap(route);
        dest.writeMap(metaData);
    }
}
