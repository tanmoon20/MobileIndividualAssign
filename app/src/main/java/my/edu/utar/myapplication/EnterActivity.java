package my.edu.utar.myapplication;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterActivity extends AppCompatActivity {

    ArrayList<Bill> billList = new ArrayList<>();
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final DecimalFormat wholedf = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        LinearLayout ll = findViewById(R.id.cardContainer);

        //create card view according to the person num
        int receivedValue = getIntent().getIntExtra("NumPerson", 0);
        for(int i = 0; i < receivedValue; i++)
        {
//            CardView cardView = (CardView) LayoutInflater.from((Context) this).inflate(R.layout.person_card, null);
            View cardView = getLayoutInflater().inflate(R.layout.person_card, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,8,8,8);
            cardView.setLayoutParams(params);

            Button btnDel = cardView.findViewById(R.id.btnDel);
            btnDel.setOnClickListener((v)->{
                ll.removeView(cardView);
            });

            //create spinner
            Spinner spinnerType = cardView.findViewById(R.id.spinner);
            ArrayAdapter<CharSequence> adapter = null;

            if(BreakDownActivity.selection[0] == true)
            {
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.type0, android.R.layout.simple_spinner_item);

                double average = BreakDownActivity.totalPrice/receivedValue;
                EditText etValue = cardView.findViewById(R.id.txtValue);
                etValue.setText(df.format(average));
                etValue.setEnabled(false);

                TextView result = cardView.findViewById(R.id.txtResult);
                result.setText(df.format(average));
            }
            else if(BreakDownActivity.selection[1] == true)
            {
                if(BreakDownActivity.selection[2] == true)
                {
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.type12, android.R.layout.simple_spinner_item);
                }
                else
                {
                    adapter = ArrayAdapter.createFromResource(this,
                            R.array.type1, android.R.layout.simple_spinner_item);
                }
            }
            else if(BreakDownActivity.selection[2] == true)
            {
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.type2, android.R.layout.simple_spinner_item);

            }
            else if(BreakDownActivity.selection[3] == true)
            {
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.type3, android.R.layout.simple_spinner_item);

                EditText etValue = cardView.findViewById(R.id.txtValue);
                etValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerType.setAdapter(adapter);

            ll.addView(cardView);
        }

       int num = ll.getChildCount();
        Toast.makeText(this, Integer.toString(num), Toast.LENGTH_SHORT).show();

        Button calculate = findViewById(R.id.btnCalculate);
        calculate.setOnClickListener((v)->{
            if(calculateBill())
            {
                Toast.makeText(this,"Calculate successfully", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener((v)->{
            if(calculateBill())
            {
                saveBill();
                Toast.makeText(this,"Save successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"Please press calculate button to calculate successfully before save", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean checkNameAndValueNotEmpty(){
        LinearLayout ll = findViewById(R.id.cardContainer);
        int cardCount = ll.getChildCount();
        boolean valid = true;

        if(cardCount == 0)
        {
            Toast.makeText(this,"No person found", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < cardCount;i++)
        {
            View personView = ll.getChildAt(i);

            EditText etName = personView.findViewById(R.id.txtName);
            EditText etValue = personView.findViewById(R.id.txtValue);

            //valid if the text is empty
            if(etName.getText().toString().equals("") || etValue.getText().toString().equals("")){
                return false;
            }

            if(Double.parseDouble(etValue.getText().toString()) < 0)
            {
                return false;
            }
        }
        return valid;
    }

    public boolean equalBreakDownAndSave(){
        String type = "Equal";

        LinearLayout ll = findViewById(R.id.cardContainer);
        int cardCount = ll.getChildCount();

        Bill bill;
        billList.clear();

        for(int i = 0; i < cardCount;i++)
        {
            View personView = ll.getChildAt(i);

            EditText etName = personView.findViewById(R.id.txtName);
            EditText etValue = personView.findViewById(R.id.txtValue);

            TextView result = personView.findViewById(R.id.txtResult);

            double average = BreakDownActivity.totalPrice/cardCount;
            etValue.setText(df.format(average));
            result.setText(df.format(average));

            bill = new Bill(etName.getText().toString(),average,type,average);

            billList.add(bill);
        }

        return true;
    }

    public boolean percentageBreakDownAndSave(){
        String type = "Percentage";

        LinearLayout ll = findViewById(R.id.cardContainer);
        int cardCount = ll.getChildCount();

        Bill bill;
        billList.clear();

        double total = 0;

        for(int i = 0; i < cardCount;i++)
        {
            View personView = ll.getChildAt(i);

            EditText etName = personView.findViewById(R.id.txtName);
            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());
            total += dValue;

            TextView result = personView.findViewById(R.id.txtResult);
            Double dResult = dValue/100 * BreakDownActivity.totalPrice;
            result.setText(df.format(dResult));

            bill = new Bill(etName.getText().toString(),dValue,type,dResult);

            billList.add(bill);
        }

        if(total > 100)
        {
            Toast.makeText(this,"Sum of percentage exceed 100%. Please minus " + wholedf.format(total - 100) + " percentage amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(total < 100)
        {
            Toast.makeText(this,"Sum of percentage is less than 100%. Please add " + wholedf.format(100 - total) + " percentage amount ", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean percentageRatioBreakDownAndSave(){

        LinearLayout ll = findViewById(R.id.cardContainer);
        int cardCount = ll.getChildCount();

        Bill bill;
        billList.clear();

        double totalPercent = 0;
        double totalRatio = 0;
        double totalPrice = BreakDownActivity.totalPrice;
        double remainingPrice = BreakDownActivity.totalPrice;

        //check if the percentage amount and ratio amount valid
        for(int i = 0; i < cardCount; i++)
        {
            View personView = ll.getChildAt(i);

            Spinner spinnerType = personView.findViewById(R.id.spinner);
            String type = spinnerType.getSelectedItem().toString();

            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());

            if(type.compareTo("Percentage") == 0)
            {
                TextView result = personView.findViewById(R.id.txtResult);

                double dResult = totalPrice * dValue/100;
                result.setText(df.format(dResult));

                totalPercent += dValue;
                remainingPrice -= dResult;
            }
            else // type = ratio
            {
                totalRatio += dValue;
            }
        }

        if(totalPercent > 100)
        {
            Toast.makeText(this,"Sum of percentage exceed 100%. Please minus " + df.format(totalPercent - 100) + " percentage amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(totalPercent == 100 && totalRatio != 0)
        {
            Toast.makeText(this,"Sum of percentage is 100% and ratio amount is set. Please minus " + df.format(totalPercent - 100) + " percentage amount / minus " + df.format(totalRatio) + " ratio amount", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(totalPercent < 100 && totalRatio == 0)
        {
            Toast.makeText(this,"Sum of percentage is less than 100%. Please add " + df.format(100 - totalPercent) + " percentage amount ", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < cardCount; i++)
        {
            View personView = ll.getChildAt(i);

            Spinner spinnerType = personView.findViewById(R.id.spinner);
            String type = spinnerType.getSelectedItem().toString();

            EditText etName = personView.findViewById(R.id.txtName);

            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());

            TextView result = personView.findViewById(R.id.txtResult);
            double dResult = 0;

            if(type.compareTo("Percentage") == 0)
            {
                dResult = Double.parseDouble(result.getText().toString());
            }
            else //type = ratio
            {
                dResult = remainingPrice * dValue/totalRatio;
                result.setText(df.format(dResult));
            }

            bill = new Bill(etName.getText().toString(),dValue,type,dResult);

            billList.add(bill);
        }

        return true;
    }

    public boolean ratioBreakDownAndSave(){
        String type = "Ratio";

        LinearLayout ll = findViewById(R.id.cardContainer);
        int cardCount = ll.getChildCount();

        Bill bill;
        billList.clear();

        double totalRatio = 0;

        for(int i = 0; i < cardCount; i++)
        {
            View personView = ll.getChildAt(i);

            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());
            totalRatio += dValue;
        }

        if(totalRatio == 0)
        {
            Toast.makeText(this,"Please enter ratio", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < cardCount;i++)
        {
            View personView = ll.getChildAt(i);

            EditText etName = personView.findViewById(R.id.txtName);
            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());

            TextView result = personView.findViewById(R.id.txtResult);
            Double dResult = BreakDownActivity.totalPrice * dValue/totalRatio;

            result.setText(df.format(dResult));

            bill = new Bill(etName.getText().toString(),dValue,type,dResult);

            billList.add(bill);
        }

        return true;
    }

    public boolean amountBreakDownAndSave(){
        String type = "Amount";

        LinearLayout ll = findViewById(R.id.cardContainer);
        int cardCount = ll.getChildCount();

        Bill bill;
        billList.clear();

        double total = BreakDownActivity.totalPrice;
        double totalAmountInput = 0;

        for(int i = 0; i < cardCount;i++)
        {

            View personView = ll.getChildAt(i);

            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());

            totalAmountInput += dValue;
        }


        if(total < totalAmountInput)
        {
            Toast.makeText(this,"You need to minus " + df.format(totalAmountInput - total) + " amount to match the total price", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(total > totalAmountInput)
        {
            Toast.makeText(this,"You need to add " + df.format(total - totalAmountInput) + " amount to match the total price", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(int i = 0; i < cardCount; i++){
            View personView = ll.getChildAt(i);

            EditText etName = personView.findViewById(R.id.txtName);
            EditText etValue = personView.findViewById(R.id.txtValue);
            double dValue = Double.parseDouble(etValue.getText().toString());


            TextView result = personView.findViewById(R.id.txtResult);
            Double dResult = dValue;
            result.setText(df.format(dResult));

            bill = new Bill(etName.getText().toString(),dValue,type,dResult);

            billList.add(bill);
        }

        return true;
    }

    public boolean calculateBill(){
        if(checkNameAndValueNotEmpty())
        {
            if(BreakDownActivity.selection[0] == true) //equal break down
            {
                return equalBreakDownAndSave();
            }
            else if(BreakDownActivity.selection[1] == true)
            {
                if(BreakDownActivity.selection[2] == true)
                {
                    return percentageRatioBreakDownAndSave();
                }
                else
                {
                    return percentageBreakDownAndSave();
                }
            }
            else if(BreakDownActivity.selection[2] == true)
            {
                return ratioBreakDownAndSave();
            }
            else if(BreakDownActivity.selection[3] == true)
            {
                return amountBreakDownAndSave();
            }
        }
        else
        {
            Toast.makeText(this,"Please enter valid name or value, or there is no person selected.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void saveBill()
    {

        SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy   HH:mm:ss");
        Date date = new Date();
        String sDate = formatter.format(date);

        String sDesc = BreakDownActivity.description;
        if(sDesc.matches(""))
        {
          sDesc = "N/A";
        }
        String sTotal = df.format(BreakDownActivity.totalPrice);

        for(int i = 0; i < billList.size(); i++)
        {
            String sName = billList.get(i).getName();
            String type = billList.get(i).getType();
            String sValue = "0";
            String sResult = "0";

            if(type.compareTo("Amount") == 0 || type.compareTo("Equal") == 0)
            {
                sValue = df.format(billList.get(i).getValue());
                sResult = df.format(billList.get(i).getResult());
            }
            else
            {
                sValue = wholedf.format(billList.get(i).getValue());
                sResult = wholedf.format(billList.get(i).getResult());
            }

            if(type.compareTo("Percentage") == 0)
            {
                type = "Percent";
            }

            mySQLiteAdapter.insert(sDate, sDesc, sName, sValue, type, sResult, sTotal);
        }

        mySQLiteAdapter.close();
    }

}

