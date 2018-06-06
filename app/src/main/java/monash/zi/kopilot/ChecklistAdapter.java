package monash.zi.kopilot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class ChecklistAdapter extends BaseAdapter implements Filterable {
    private Context currentContext;
    private ArrayList<Checklist> checklistArrayList;
    private ArrayList<Checklist> filteredchecklistArrayList;
    private ChecklistFilter filter;

    ChecklistAdapter(Context con, ArrayList<Checklist> book) {
        currentContext = con;
        checklistArrayList = book;
        filteredchecklistArrayList = checklistArrayList;

    }
    @Override
    public int getCount() { return filteredchecklistArrayList.size(); }

    @Override
    public Object getItem(int i) { return filteredchecklistArrayList.get(i); }

    @Override
    public long getItemId(int i) { return i; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // Check if view already exists. If not inflate it
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) currentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Create a list item based off layout definition
            assert inflater != null;
            view = inflater.inflate(R.layout.list_view_checklist_item, null);
        }
        // Assign values to the TextViews using Book Object
        TextView checklistItemTextView = view.findViewById(R.id.checklistItemTitleTextView);
        TextView checklistDescriptionTextView = view.findViewById(R.id.checklistItemDescriptionTextView);
        checklistItemTextView.setText(filteredchecklistArrayList.get(i).getChecklistName());
        checklistDescriptionTextView.setText(filteredchecklistArrayList.get(i).getChecklistDescription());

        return view;
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new ChecklistFilter();
        }
        return filter;
    }

    private class ChecklistFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                ArrayList<Checklist> tempList = new ArrayList<>();
                for(Checklist checklist: checklistArrayList) {
                    if(checklist.getChecklistName().toLowerCase().
                            contains(constraint.toString().
                                    toLowerCase()))
                        tempList.add(checklist);
                }
                results.count = tempList.size();
                results.values = tempList;
            }
            else {
                results.count = checklistArrayList.size();
                results.values = checklistArrayList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredchecklistArrayList = (ArrayList<Checklist>) results.values;
            notifyDataSetChanged();
        }
    }
}
