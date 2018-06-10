package monash.zi.kopilot;


import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

class LocDetails {
    protected String locationName;
    protected String locationDesc;
    protected LatLng latLng;

    //    private String routeName;
    //    private String routeDescription;
    //    private String startPlanet;
    //    private String destPlanet;
    //    private String creator;
    //    private String latitude;
    //    private String longitude;

    private Map route;
    private Map metaData;
    private Map createLocation;

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
}
