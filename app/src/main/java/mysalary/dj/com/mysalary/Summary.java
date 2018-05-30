package mysalary.dj.com.mysalary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Summary extends Fragment {

    private Spinner spinner;
    DatabaseHelper db;
    TextView monthlySpent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary, container, false);

        monthlySpent = (TextView)rootView.findViewById(R.id.monthlySpending);
        spinner = (Spinner) rootView.findViewById(R.id.records);

        db = new DatabaseHelper(getActivity());

        refreshDate();
        refreshContent();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                refreshContent();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshDate();
        refreshContent();
    }

    public void refreshDate(){
        Cursor readMonthCursor = db.readMonth();
        String[] date;

        if(readMonthCursor.moveToFirst()){
            date = new String[readMonthCursor.getCount()];
            int i = 0;
            do {
                date[i] = (String.valueOf(readMonthCursor.getString(readMonthCursor.getColumnIndex("month"))));
                i++;
            }while(readMonthCursor.moveToNext());
        }
        else{
            date = new String[1];
            date[0] = "No Record Found";
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(date));

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

    }

    public void refreshContent(){
        String month = "1/1";
        String part1;
        String part2;
        if(spinner.getSelectedItem()!=null)
            month = spinner.getSelectedItem().toString();

        String[] parts = month.split("/");
        if(parts.length>1) {
            part1 = parts[0];
            part2 = parts[1];
        }
        else {
            part1 = parts[0];
            part2 = "";
        }
        String dateYear = part1 + "-" + part2;
        switch(Integer.parseInt(part2)) {
            case 1:
                part2 = "Jan";
                break;
            case 2:
                part2 = "Feb";
                break;
            case 3:
                part2 = "Mar";
                break;
            case 4:
                part2 = "Apr";
                break;
            case 5:
                part2 = "May";
                break;
            case 6:
                part2 = "Jun";
                break;
            case 7:
                part2 = "Jul";
                break;
            case 8:
                part2 = "Aug";
                break;
            case 9:
                part2 = "Sep";
                break;
            case 10:
                part2 = "Oct";
                break;
            case 11:
                part2 = "Nov";
                break;
            case 12:
                part2 = "Dec";
                break;
        }

        Cursor cursor = db.readSpending(dateYear);
        if(cursor.moveToFirst()){
            String[] spent = new String[cursor.getCount()];

            int i = 0;
            double totalSpent = 0;
            do {
                spent[i] = cursor.getString(cursor.getColumnIndex("amount"));
                spent[i] += cursor.getString(cursor.getColumnIndex("category"));
                totalSpent += Double.parseDouble(cursor.getString(cursor.getColumnIndex("amount")));
                i++;
            }while(cursor.moveToNext());
            //monthlySpent.setText(Arrays.toString(spent));
            monthlySpent.setText("Your spending for "+part2+" "+part1+" is : "+String.valueOf(totalSpent));
        }

    }
}
