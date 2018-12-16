package mealorder.odiljon.com.tastyserver;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import mealorder.odiljon.com.tastyserver.Model.User;

public class SignIn extends AppCompatActivity {
    MaterialEditText phoneEdt;
    MaterialEditText passwordEdt;
    Button signInBtn;

    FirebaseDatabase database;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        phoneEdt = (MaterialEditText)findViewById(R.id.sign_in_phone_edit);
        passwordEdt = (MaterialEditText)findViewById(R.id.sign_in_password_edit);
        signInBtn = (Button)findViewById(R.id.sign_in_sign_in_button);

        //Initialize firebase
        database = FirebaseDatabase.getInstance();
        user = database.getReference("User");

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(phoneEdt.getText().toString(), passwordEdt.getText().toString());
            }
        });

    }

    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage(">> >> WAIT >> >>");
        mDialog.show();

        final String localPhone = phone;
        final String localPassword = password;

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if(Boolean.parseBoolean(user.getIsStaff()))// If IsStaff == true
                    {
                        if(user.getPassword().equals(localPassword))
                        {
                            //Login succes
                            Toast.makeText(SignIn.this, "! SUCCESS !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Login fail
                            Toast.makeText(SignIn.this, "! WRONG PASSWORD !", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(SignIn.this, "! LOG IN WITH STUFF ACCOUNT !", Toast.LENGTH_SHORT).show();
                    }


                }
                else
                {
                    mDialog.dismiss();
                    Toast.makeText(SignIn.this, "! USER DOES NOT EXIST !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
