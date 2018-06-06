package monash.zi.kopilot;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Route implements Parcelable {

    private String routeName;

    private String routeDescription;
    private String startPlanet;
    private String destPlanet;
    private ArrayList<String> stops;

    protected Route(Parcel in) {
        routeName = in.readString();
        routeDescription = in.readString();
        startPlanet = in.readString();
        destPlanet = in.readString();
        stops = in.createStringArrayList();
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
        dest.writeString(routeName);
        dest.writeString(routeDescription);
        dest.writeString(startPlanet);
        dest.writeString(destPlanet);
        dest.writeStringList(stops);
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteDescription() {
        return routeDescription;
    }

    public void setRouteDescription(String routeDescription) {
        this.routeDescription = routeDescription;
    }

    public String getStartPlanet() {
        return startPlanet;
    }

    public void setStartPlanet(String startPlanet) {
        this.startPlanet = startPlanet;
    }

    public String getDestPlanet() {
        return destPlanet;
    }

    public void setDestPlanet(String destPlanet) {
        this.destPlanet = destPlanet;
    }

    public ArrayList<String> getStops() {
        return stops;
    }

    public void setStops(ArrayList<String> stops) {
        this.stops = stops;
    }

}
