package monash.zi.kopilot;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

class Route implements Parcelable{
    protected Map route;
    protected Map metaData;
    protected Map createLocation;

    protected String locationName;
    protected String locationDesc;
    protected LatLng latLng;

    public Route(String inLocationName, LatLng inLatLng) {
        this.locationName = inLocationName;
        this.latLng = inLatLng;
    }

    public Route(Map jsonMap) {
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

    protected Route(Parcel in) {
        route = new HashMap<String, Object>();
        in.readMap(route, Object.class.getClassLoader());
        metaData = new HashMap<String, Object>();
        in.readMap(metaData, Object.class.getClassLoader());
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(route);
        dest.writeMap(metaData);
    }
}
