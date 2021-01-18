package com.example.wallet10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    private String selectedValue, firstNameInput, lastNameInput, passwordInput, conPasswordInput, phoneInput;

    private String myPhoneNumber;
    EditText regEditUserName, regEditLastName, regEditPassword, regEditConPassword, editPhone;
    final Context context = this;
    private Pattern onlyChars, passwordCheck, hkPhoneCheck, chinaPhoneCheck, MoroccanPhoneCheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        regEditUserName = (EditText)findViewById(R.id.regEditUserName);
        regEditLastName = (EditText)findViewById(R.id.regEditLastName);
        regEditPassword = (EditText)findViewById(R.id.regEditPassword);
        regEditConPassword = (EditText)findViewById(R.id.regEditConPassword);
        editPhone = (EditText)findViewById(R.id.editPhone);
        //regSpinner = (Spinner)findViewById(R.id.regSpinner);



        /* restore inputs */
        if(this.getIntent().getExtras() != null){
            regEditUserName.setText(this.getIntent().getStringExtra("firstName"));
            regEditLastName.setText(this.getIntent().getStringExtra("lastName"));
            regEditPassword.setText(this.getIntent().getStringExtra("password"));
            regEditConPassword.setText(this.getIntent().getStringExtra("conPassword"));
            editPhone.setText(this.getIntent().getStringExtra("phone"));
            Log.i(" ", this.getIntent().getStringExtra("selectedItem"));

        }

        /* regular express */
        onlyChars = Pattern.compile("^[A-Z]*[a-z]*$");
        passwordCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{4,20}");
        hkPhoneCheck = Pattern.compile("^1[0-9]{10}$|^[569][0-9]{7}$");
        chinaPhoneCheck = Pattern.compile("^(13[0-9]|14[57]|15[012356789]|17[0678]|18[0-9])[0-9]{8}$");
        MoroccanPhoneCheck = Pattern.compile("");

        /* listeners */
        regEditUserName.setOnClickListener(this);
        regEditLastName.setOnClickListener(this);


        Button REGSIGNINBUTTON = (Button)findViewById(R.id.regSignInButton);
        REGSIGNINBUTTON.setOnClickListener(this);



        Button REGFORWARDBUTTON = (Button)findViewById(R.id.regForwardButton);
        REGFORWARDBUTTON.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.regSignInButton){
            Intent goToSignIn = new Intent(this, SignIn.class);
            startActivity(goToSignIn);
        }
        else if(view.getId() == R.id.regForwardButton){
            firstNameInput = regEditUserName.getText().toString();
            lastNameInput = regEditLastName.getText().toString();
            passwordInput = regEditPassword.getText().toString();
            conPasswordInput = regEditConPassword.getText().toString();
            phoneInput = editPhone.getText().toString();
            if(canGoSMSCon(firstNameInput, lastNameInput, passwordInput, conPasswordInput, phoneInput)) {
                Intent goToSMSCon = new Intent(this, SMSConfirmation.class);
                goToSMSCon.putExtra("firstName", firstNameInput);
                goToSMSCon.putExtra("lastName", lastNameInput);
                goToSMSCon.putExtra("password", passwordInput);
                goToSMSCon.putExtra("conPassword", conPasswordInput);
                goToSMSCon.putExtra("phone", phoneInput);
                goToSMSCon.putExtra("selectedItem", selectedValue);

                startActivity(goToSMSCon);
            }
            else{
                AlertDialog.Builder regValidityBuilder = new AlertDialog.Builder(context);
                regValidityBuilder
                        .setMessage("One/Some of your inputs is/are invalid")
                        .setTitle("Proceed rejected")
                        .setCancelable(true)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog regValidityBuilderDialog = regValidityBuilder.create();
                regValidityBuilderDialog.show();

            }
        }
        else if(view.getId() == R.id.regEditUserName){
            regEditUserName.setCursorVisible(true);
        }
        else if(view.getId() == R.id.regEditLastName){
            regEditLastName.setCursorVisible(true);
        }
    }
    private boolean isFirsttNameValid(String firstNameInput){
        Matcher onlyCharsMatcher = onlyChars.matcher(firstNameInput);
        if(!TextUtils.isEmpty(firstNameInput) && onlyCharsMatcher.find()) {
            return true;
        }
        else {
            regEditUserName.setError("Your first name must start with capital letter and should not be empty");
            return false;
        }
    }

    private boolean isLastNameValid(String lastNameInput){
        Matcher onlyCharsMatcher = onlyChars.matcher(lastNameInput);
        if(!TextUtils.isEmpty(lastNameInput) && onlyCharsMatcher.find()) {
            return true;
        }
        else {
            regEditLastName.setError("Your last name must start with capital letter and should not be empty");
            return false;
        }

    }

    private boolean isPasswordValid(String passwordInput){
        Matcher passwordMatcher = passwordCheck.matcher(passwordInput);
        if(!TextUtils.isEmpty(passwordInput) && passwordMatcher.find()) {
            return true;
        }
        else {
            regEditPassword.setError("Your password should contain digit(s), capital and small letter(s), and should have at least a length of 4");
            return false;
        }

    }

    private boolean isConPasswordValid(String passwordInput, String conPasswordInput){
        Matcher conPasswordMatcher = passwordCheck.matcher(conPasswordInput);
        if(!TextUtils.isEmpty(conPasswordInput) && conPasswordMatcher.find() && conPasswordInput.equals(passwordInput)) {
            return true;
        }
        else {
            regEditConPassword.setError("Your confirm password should be the same as password and should not be empty");
            return false;
        }

    }

    private boolean isPhoneValid(String phoneInput){
        //TelephonyManager myPhone = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //myPhoneNumber = myPhone.getLine1Number();
        //Log.i("phone", myPhoneNumber);

            Matcher conPhoneMatcher = MoroccanPhoneCheck.matcher(phoneInput);
            if(conPhoneMatcher.find()){return true;} //&& myPhoneNumber.equals(phoneInput)
            else{
                editPhone.setError("Your phone number is invalid or empty");
                return false;
            }


    }

    private boolean canGoSMSCon(String firstNameInput, String lastNameInput, String passwordInput, String conPasswordInput, String phoneInput){
        if(isFirsttNameValid(firstNameInput) && isLastNameValid(lastNameInput) && isPasswordValid(passwordInput) && isConPasswordValid(passwordInput, conPasswordInput) && isPhoneValid(phoneInput)){return true;}
        else{return false;}
    }

    @Override
    public void onBackPressed() {
        // do not allow android backward button
    }
}
