package my.edu.utar.myapplication;

import android.widget.ImageView;

public class Bill {
    String name = "Anonymous";
    double value = 0.00;
    String type;
    double result = 0.00;

    public Bill() {}

    public Bill(String type){
        this.type = type;
    }

    public Bill(String name,  double value, String type, double result)
    {
        this.name = name;
        this.type = type;
        this.value = value;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getValue(){return value;}

    public void setValue(double value){this.value = value;}

    public double getResult() {return result;}

    public void setResult(double result) {this.result = result;}

    public void setbtnRemovePerson(ImageView btnRemovePerson){
        setbtnRemovePerson(btnRemovePerson);
    }
}
