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

    TextInputLayout custfname,custlname,custEmail,custPass,custcpass,custmobileno,custlocaladd,custarea,custpincode;
    Spinner custStatespin,custCityspin;
    Button custsignreg, custEmaill;
    CountryCodePicker custCpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String Custfname,Custlname,Custemailid,Custpassword,Custconfpassword,Custmobile,CustLocaladdress,CustArea,CustPincode,Custstatee,Custcityy;
    String role="Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        custfname = (TextInputLayout) findViewById(R.id.CustomerFirstname);
        custlname = (TextInputLayout) findViewById(R.id.CustomerLastname);
        custEmail = (TextInputLayout) findViewById(R.id.CustomerEmail);
        custPass = (TextInputLayout) findViewById(R.id.Password);
        custcpass = (TextInputLayout) findViewById(R.id.CustomerConfirm);
        custmobileno = (TextInputLayout) findViewById(R.id.CustomerNumber);
        custlocaladd = (TextInputLayout) findViewById(R.id.LocalAddress);
        custpincode = (TextInputLayout) findViewById(R.id.PinsCode);
        custStatespin = (Spinner) findViewById(R.id.States);
        custCityspin = (Spinner) findViewById(R.id.Cities);
        custarea = (TextInputLayout) findViewById(R.id.Area);

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

                Custfname = custfname.getEditText().getText().toString().trim();
                Custlname = custlname.getEditText().getText().toString().trim();
                Custemailid = custEmail.getEditText().getText().toString().trim();
                Custmobile = custmobileno.getEditText().getText().toString().trim();
                Custpassword = custPass.getEditText().getText().toString().trim();
                Custconfpassword = custcpass.getEditText().getText().toString().trim();
                CustArea = custarea.getEditText().getText().toString().trim();
                CustLocaladdress = custlocaladd.getEditText().getText().toString().trim();
                CustPincode = custpincode.getEditText().getText().toString().trim();

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
                                        hashMap1.put("First Name", Custfname);
                                        hashMap1.put("Last Name", Custlname);
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
        custEmail.setErrorEnabled(false);
        custEmail.setError("");
        custfname.setErrorEnabled(false);
        custfname.setError("");
        custlname.setErrorEnabled(false);
        custlname.setError("");
        custPass.setErrorEnabled(false);
        custPass.setError("");
        custmobileno.setErrorEnabled(false);
        custmobileno.setError("");
        custcpass.setErrorEnabled(false);
        custcpass.setError("");
        custarea.setErrorEnabled(false);
        custarea.setError("");
        custlocaladd.setErrorEnabled(false);
        custlocaladd.setError("");
        custpincode.setErrorEnabled(false);
        custpincode.setError("");

        boolean isValid=false,isValidCustlocaladd=false,isValidCustlname=false,isValidCustname=false,isValidCustemail=false,isValidCustpassword=false,isValidCustconfpassword=false,isValidCustmobilenum=false,isValidCustarea=false,isValidCustpincode=false;
        if(TextUtils.isEmpty(Custfname)){
            custfname.setErrorEnabled(true);
            custfname.setError("Enter First Name");
        }else{
            isValidCustname = true;
        }
        if(TextUtils.isEmpty(Custlname)){
            custlname.setErrorEnabled(true);
            custlname.setError("Enter Last Name");
        }else{
            isValidCustlname = true;
        }
        if(TextUtils.isEmpty(Custemailid)){
            custEmail.setErrorEnabled(true);
            custEmail.setError("Email Is Required");
        }else{
            if(Custemailid.matches(emailpattern)){
                isValidCustemail = true;
            }else{
                custEmail.setErrorEnabled(true);
                custEmail.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(Custpassword)){
            custPass.setErrorEnabled(true);
            custPass.setError("Enter Password");
        }else{
            if(Custpassword.length()<8){
                custPass.setErrorEnabled(true);
                custPass.setError("Password is Weak");
            }else{
                isValidCustpassword = true;
            }
        }
        if(TextUtils.isEmpty(Custconfpassword)){
            custcpass.setErrorEnabled(true);
            custcpass.setError("Enter Password Again");
        }else{
            if(!Custpassword.equals(Custconfpassword)){
                custcpass.setErrorEnabled(true);
                custcpass.setError("Password Dosen't Match");
            }else{
                isValidCustconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(Custmobile)){
            custmobileno.setErrorEnabled(true);
            custmobileno.setError("Mobile Number Is Required");
        }else{
            if(Custmobile.length()<10){
                custmobileno.setErrorEnabled(true);
                custmobileno.setError("Invalid Mobile Number");
            }else{
                isValidCustmobilenum = true;
            }
        }
        if(TextUtils.isEmpty(CustArea)){
            custarea.setErrorEnabled(true);
            custarea.setError("Area Is Required");
        }else{
            isValidCustarea = true;
        }
        if(TextUtils.isEmpty(CustPincode)){
            custpincode.setErrorEnabled(true);
            custpincode.setError("Please Enter Pincode");
        }else{
            isValidCustpincode = true;
        }
        if(TextUtils.isEmpty(CustLocaladdress)){
            custlocaladd.setErrorEnabled(true);
            custlocaladd.setError("Fields Can't Be Empty");
        }else{
            isValidCustlocaladd = true;
        }

        isValid = (isValidCustarea && isValidCustconfpassword && isValidCustpassword && isValidCustpincode && isValidCustemail && isValidCustmobilenum && isValidCustname && isValidCustlocaladd && isValidCustlname) ? true : false;
        return isValid;


    }

}
