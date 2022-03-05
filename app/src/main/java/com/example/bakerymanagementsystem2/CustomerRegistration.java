package com.example.bakerymanagementsystem2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerRegistration extends AppCompatActivity {
    String[] Maharashtra = {"Mumbai","Pune","Nashik"};
    String[] Gujarat = {"Ahmedabad","Baroda","Surat"};

    TextInputLayout Custfname,Custlname,CustEmail,CustPass,Custcpass,Custmobileno,Custlocaladd,Custarea,Custpincode;
    Spinner custStatespin,custCityspin;
    Button custsignreg, custEmaill;
    CountryCodePicker custCpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String CustFname,CustLname,Custemailid,Custpassword,Custconfpassword,Custmobile,CustLocaladdress,CustArea,CustPincode,Custstatee,Custcityy;
    String role="Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        Custfname = (TextInputLayout) findViewById(R.id.CustomerFirstname);
        Custlname = (TextInputLayout) findViewById(R.id.CustomerLastname);
        CustEmail = (TextInputLayout) findViewById(R.id.CustomerEmailid);
        CustPass = (TextInputLayout) findViewById(R.id.Password);
        Custcpass = (TextInputLayout) findViewById(R.id.CustomerConfirm);
        Custmobileno = (TextInputLayout) findViewById(R.id.CustomerNumber);
        Custlocaladd = (TextInputLayout) findViewById(R.id.LocalAddress);
        Custpincode = (TextInputLayout) findViewById(R.id.PinsCode);
        custStatespin = (Spinner) findViewById(R.id.States);
        custCityspin = (Spinner) findViewById(R.id.Cities);
        Custarea = (TextInputLayout) findViewById(R.id.Area);
        custCpp = (CountryCodePicker) findViewById(R.id.CountryCode);
        custsignreg = (Button) findViewById(R.id.CustomerRegister);
        custEmaill = (Button) findViewById(R.id.email);


        custStatespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getItemAtPosition(position);
                Custstatee = value.toString().trim();
                if (Custstatee.equals("Maharashtra")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String cities : Maharashtra) {
                        list.add(cities);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerRegistration.this, android.R.layout.simple_spinner_item, list);
                    custCityspin.setAdapter(arrayAdapter);
                }
                if (Custstatee.equals("Gujarat")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String cities : Gujarat) {
                        list.add(cities);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerRegistration.this, android.R.layout.simple_spinner_item, list);
                    custCityspin.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        custCityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                Custcityy = value.toString().trim();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = firebaseDatabase.getInstance().getReference("Customer");
        FAuth = FirebaseAuth.getInstance();

        custsignreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustFname = Custfname.getEditText().getText().toString().trim();
                CustLname = Custlname.getEditText().getText().toString().trim();
                Custemailid = CustEmail.getEditText().getText().toString().trim();
                Custmobile = Custmobileno.getEditText().getText().toString().trim();
                Custpassword = CustPass.getEditText().getText().toString().trim();
                Custconfpassword = Custcpass.getEditText().getText().toString().trim();
                CustArea = Custarea.getEditText().getText().toString().trim();
                CustLocaladdress = Custlocaladd.getEditText().getText().toString().trim();
                CustPincode = Custpincode.getEditText().getText().toString().trim();

                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(CustomerRegistration.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registration in progress please wait......");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(Custemailid, Custpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                        hashMap1.put("Mobile No", Custmobile);
                                        hashMap1.put("First Name", CustFname);
                                        hashMap1.put("Last Name", CustLname);
                                        hashMap1.put("EmailId", Custemailid);
                                        hashMap1.put("City", Custcityy);
                                        hashMap1.put("Area", CustArea);
                                        hashMap1.put("Password", Custpassword);
                                        hashMap1.put("PinsCode", CustPincode);
                                        hashMap1.put("State", Custstatee);
                                        hashMap1.put("Confirm Password", Custconfpassword);
                                        hashMap1.put("Local Address", CustLocaladdress);

                                        firebaseDatabase.getInstance().getReference("Customer")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();

                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerRegistration.this);
                                                            builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                    dialog.dismiss();

                                                                   /* String phonenumber = custCpp.getSelectedCountryCodeWithPlus() + Custmobile;
                                                                    Intent b = new Intent(Registration.this, CustomerVerifyPhone.class);
                                                                    b.putExtra("phonenumber", phonenumber);
                                                                    startActivity(b);*/

                                                                }
                                                            });
                                                            AlertDialog Alert = builder.create();
                                                            Alert.show();
                                                        } else {
                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(CustomerRegistration.this, "Error", task.getException().getMessage());
                                                        }
                                                    }
                                                });

                                            }
                                        });

                                    }
                                });
                            } else {
                                mDialog.dismiss();
                                ReusableCodeForAll.ShowAlert(CustomerRegistration.this, "Error", task.getException().getMessage());
                            }
                        }
                    });
                }

            }
        });

        custEmaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerRegistration.this, CustomerLogin.class));
                finish();
            }
        });

    }


    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){
        CustEmail.setErrorEnabled(false);
        CustEmail.setError("");
        Custfname.setErrorEnabled(false);
        Custfname.setError("");
        Custlname.setErrorEnabled(false);
        Custlname.setError("");
        CustPass.setErrorEnabled(false);
        CustPass.setError("");
        Custmobileno.setErrorEnabled(false);
        Custmobileno.setError("");
        Custcpass.setErrorEnabled(false);
        Custcpass.setError("");
        Custarea.setErrorEnabled(false);
        Custarea.setError("");
        Custlocaladd.setErrorEnabled(false);
        Custlocaladd.setError("");
        Custpincode.setErrorEnabled(false);
        Custpincode.setError("");

        boolean isValid=false,isValidCustlocaladd=false,isValidCustlname=false,isValidCustname=false,isValidCustemail=false,isValidCustpassword=false,isValidCustconfpassword=false,isValidCustmobilenum=false,isValidCustarea=false,isValidCustpincode=false;
        if(TextUtils.isEmpty(CustFname)){
            Custfname.setErrorEnabled(true);
            Custfname.setError("Enter First Name");
        }else{
            isValidCustname = true;
        }
        if(TextUtils.isEmpty(CustLname)){
            Custlname.setErrorEnabled(true);
            Custlname.setError("Enter Last Name");
        }else{
            isValidCustlname = true;
        }
        if(TextUtils.isEmpty(Custemailid)){
            CustEmail.setErrorEnabled(true);
            CustEmail.setError("Email Is Required");
        }else{
            if(Custemailid.matches(emailpattern)){
                isValidCustemail = true;
            }else{
                CustEmail.setErrorEnabled(true);
                CustEmail.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(Custpassword)){
            CustPass.setErrorEnabled(true);
            CustPass.setError("Enter Password");
        }else{
            if(Custpassword.length()<8){
                CustPass.setErrorEnabled(true);
                CustPass.setError("Password is Weak");
            }else{
                isValidCustpassword = true;
            }
        }
        if(TextUtils.isEmpty(Custconfpassword)){
            Custcpass.setErrorEnabled(true);
            Custcpass.setError("Enter Password Again");
        }else{
            if(!Custpassword.equals(Custconfpassword)){
                Custcpass.setErrorEnabled(true);
                Custcpass.setError("Password Dosen't Match");
            }else{
                isValidCustconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(Custmobile)){
            Custmobileno.setErrorEnabled(true);
            Custmobileno.setError("Mobile Number Is Required");
        }else{
            if(Custmobile.length()<10){
                Custmobileno.setErrorEnabled(true);
                Custmobileno.setError("Invalid Mobile Number");
            }else{
                isValidCustmobilenum = true;
            }
        }
        if(TextUtils.isEmpty(CustArea)){
            Custarea.setErrorEnabled(true);
            Custarea.setError("Area Is Required");
        }else{
            isValidCustarea = true;
        }
        if(TextUtils.isEmpty(CustPincode)){
            Custpincode.setErrorEnabled(true);
            Custpincode.setError("Please Enter Pincode");
        }else{
            isValidCustpincode = true;
        }
        if(TextUtils.isEmpty(CustLocaladdress)){
            Custlocaladd.setErrorEnabled(true);
            Custlocaladd.setError("Fields Can't Be Empty");
        }else{
            isValidCustlocaladd = true;
        }

        isValid = (isValidCustarea && isValidCustconfpassword && isValidCustpassword && isValidCustpincode && isValidCustemail && isValidCustmobilenum && isValidCustname && isValidCustlocaladd && isValidCustlname) ? true : false;
        return isValid;


    }

}
