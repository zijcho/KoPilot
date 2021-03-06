package monash.zi.kopilot;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Checklist implements Parcelable{
    // Todo: local persistence or otherwise.

    private String checklistName;
    private String checklistDescription;
    private ArrayList<String> checklistItems;

    protected Checklist(Parcel in) {
        checklistName = in.readString();
        checklistDescription = in.readString();
        checklistItems = in.createStringArrayList();
    }

    public static final Creator<Checklist> CREATOR = new Creator<Checklist>() {
        @Override
        public Checklist createFromParcel(Parcel in) {
            return new Checklist(in);
        }

        @Override
        public Checklist[] newArray(int size) {
            return new Checklist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(checklistName);
        parcel.writeString(checklistDescription);
        parcel.writeStringList(checklistItems);
    }

    public String getChecklistName() {
        return checklistName;
    }

    public void setChecklistName(String checklistName) {
        this.checklistName = checklistName;
    }

    public String getChecklistDescription() {
        return checklistDescription;
    }

    public void setChecklistDescription(String checklistDescription) {
        this.checklistDescription = checklistDescription;
    }

    public ArrayList<String> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(ArrayList<String> checklistItems) {
        this.checklistItems = checklistItems;
    }
}
