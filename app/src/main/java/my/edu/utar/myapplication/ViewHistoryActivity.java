package my.edu.utar.myapplication;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class ViewHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(this);

        TableLayout tablelayout = findViewById(R.id.tableContainer);
        tablelayout.setStretchAllColumns(true);

        //set table row params
        TableLayout.LayoutParams tableRowParams=
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.MATCH_PARENT);

        int leftMargin=4;
        int topMargin=4;
        int rightMargin=4;
        int bottomMargin=4;

        tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

        //read data into table layout
        mySQLiteAdapter.openToRead();
        String contentRead = mySQLiteAdapter.queueAll();
        List<String> database_item = Arrays.asList(contentRead.split(","));

        for(int i = 0; i < database_item.size(); ){
            TableRow row = new TableRow(this);
            row.setLayoutParams(tableRowParams);
            row.setBackgroundColor(Color.WHITE);

            for(int j = 0; j < 7; j ++)
            {
                TextView tv1 = new TextView(this);

                //is datetime, add new line
                if(j == 0)
                {
                    String dateTime = database_item.get(i++);
                    dateTime = dateTime.replace("   ", "\n");
                    tv1.setText(dateTime);
                }
                else
                {
                    tv1.setText(database_item.get(i++));
                }

                row.addView(tv1);
            }

            tablelayout.addView(row);
        }
        mySQLiteAdapter.close();
    }
}