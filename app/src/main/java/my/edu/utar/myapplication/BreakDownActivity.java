package my.edu.utar.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BreakDownActivity extends AppCompatActivity {

    public static Boolean[] selection = {false, false, false, false};
    public static double totalPrice = 0.0;
    public static String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_down);

        //display current dateTime
        TextView dateTimeTV = findViewById(R.id.dateTime);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   HH:mm:ss");
        Date date = new Date();
        dateTimeTV.setText(formatter.format(date));

        //add Person
        FloatingActionButton btnAddPerson = findViewById(R.id.btnAddPerson);
        btnAddPerson.setOnClickListener((v)->{

            TextView descriptionTV = findViewById(R.id.editTextTextDescription);
            description = descriptionTV.getText().toString();

            CheckBox equalCB = findViewById(R.id.equalCheckBox);
            CheckBox percentCB = findViewById(R.id.percentCheckbox);
            CheckBox ratioCB = findViewById(R.id.ratioCheckBox);
            CheckBox amountCB = findViewById(R.id.amountCheckBox);

            if(!(equalCB.isChecked()||percentCB.isChecked()||ratioCB.isChecked()||amountCB.isChecked()))
            {
                Toast.makeText(this, "Please check any checkbox\n(equal/percent/percent+ratio/ratio/amount)", Toast.LENGTH_SHORT).show();
            }
            else
            {
                EditText etNumPerson = findViewById(R.id.editTextNumPeople);
                String numPeople = etNumPerson.getText().toString();

                EditText etTotalPrice = findViewById(R.id.editTextTotalPrice);

                if (numPeople.matches("")) {
                    Toast.makeText(this, "Please enter number of people", Toast.LENGTH_SHORT).show();
                }
                else if(Integer.parseInt(numPeople) <= 0)
                {
                    Toast.makeText(this, "Please enter valid number of people", Toast.LENGTH_SHORT).show();
                }
                else if(etTotalPrice.getText().toString().matches(""))
                {
                    Toast.makeText(this, "Please enter total price besides the dollar sign", Toast.LENGTH_SHORT).show();
                }
                else if(Double.parseDouble(etTotalPrice.getText().toString()) <= 0)
                {
                    Toast.makeText(this, "Please enter valid total price besides the dollar sign", Toast.LENGTH_SHORT).show();
                }
                else {
                    totalPrice = Double.parseDouble(etTotalPrice.getText().toString());
                    int num =  Integer.parseInt(numPeople);
//                Toast.makeText(getApplicationContext(), Integer.toString(num), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BreakDownActivity.this, EnterActivity.class);
                    intent.putExtra("NumPerson", num);
                    startActivity(intent);
                }
            }
        });
    }

    //Checkbox on Checked
    public void EqualCBClicked(View v)
    {
        CheckBox percentCB = findViewById(R.id.percentCheckbox);
        CheckBox ratioCB = findViewById(R.id.ratioCheckBox);
        CheckBox amountCB = findViewById(R.id.amountCheckBox);

        TextView titleTV = findViewById(R.id.title);
        titleTV.setText(R.string.equal_title);
        percentCB.setEnabled(false);
        ratioCB.setEnabled(false);
        amountCB.setEnabled(false);
        if(!((CheckBox)v).isChecked())
        {
            titleTV.setText(R.string.breakdown_title);
            percentCB.setEnabled(true);
            ratioCB.setEnabled(true);
            amountCB.setEnabled(true);
            selection[0] = false;
        }
        else
        {
            selection[0] = true;
        }
    }

    public void CustomCBClicked(View v)
    {
        CheckBox equalCB = findViewById(R.id.equalCheckBox);
        CheckBox percentCB = findViewById(R.id.percentCheckbox);
        CheckBox ratioCB = findViewById(R.id.ratioCheckBox);
        CheckBox amountCB = findViewById(R.id.amountCheckBox);

        TextView titleTV = findViewById(R.id.title);
        titleTV.setText(R.string.custom_title);

        equalCB.setEnabled(false);

        if(!((CheckBox)v).isChecked())
        {
            if(!((percentCB.isChecked() || ratioCB.isChecked())))
            {
                equalCB.setEnabled(true);
                titleTV.setText(R.string.breakdown_title);
                //false
            }
        }

        if(percentCB.isChecked() || ratioCB.isChecked())
        {
            amountCB.setEnabled(false);
            if(percentCB.isChecked())
            {
                selection[1] = true;
                if(ratioCB.isChecked())
                {
                    selection[2] = true;
                }
                else
                {
                    selection[2] = false;
                }
            }
            else
            {
                selection[1] = false;
                selection[2] = true;
            }
        }
        else
        {
            amountCB.setEnabled(true);
            selection[1] = false;
            selection[2] = false;
        }

        if(amountCB.isChecked())
        {
            percentCB.setEnabled(false);
            ratioCB.setEnabled(false);
            selection[3] = true;
        }
        else
        {
            percentCB.setEnabled(true);
            ratioCB.setEnabled(true);
            selection[3] = false;
        }
    }
}