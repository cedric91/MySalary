package mysalary.dj.com.mysalary;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class Summary extends Fragment {

    DatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.summary, container, false);

        TextView monthlySpent = (TextView)rootView.findViewById(R.id.monthlySpending);
        db = new DatabaseHelper(getActivity());
        Cursor cursor = db.readSpending();
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
