package mysalary.dj.com.mysalary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary, container, false);

        TextView monthlySpent = (TextView)rootView.findViewById(R.id.monthlySpending);
        spinner = (Spinner) rootView.findViewById(R.id.records);

        db = new DatabaseHelper(getActivity());
        Cursor cursor = db.readSpending();
        Cursor readMonthCursor = db.readMonth();

        if(readMonthCursor.moveToFirst()){
            String[] date = new String[readMonthCursor.getCount()];
            int i = 0;
            do {
                date[i] = (String.valueOf(readMonthCursor.getString(cursor.getColumnIndex("month"))));
                i++;
            }while(readMonthCursor.moveToNext());

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Arrays.asList(date));

            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // attaching data adapter to spinner
            spinner.setAdapter(dataAdapter);
        }

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
            monthlySpent.setText("Your spending for January is : "+String.valueOf(totalSpent));
        }

        return rootView;
    }
}
