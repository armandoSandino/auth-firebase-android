package com.sandino.firebaseauthui.FRAGMENTS;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.installations.FirebaseInstallationsRegistrar;
import com.sandino.firebaseauthui.R;

public class RegisterAccount extends Fragment {
    private FirebaseAuth  firebaseAuth;
    TextInputEditText ctEmail;
    TextInputEditText ctPassword;

    public RegisterAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firebaseAuth =  FirebaseAuth.getInstance();

        return inflater.inflate(R.layout.fragment_register_account, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final NavController navController = Navigation.findNavController( view );
        ctEmail = view.findViewById(R.id.ctEmail);
        ctPassword = view.findViewById(R.id.ctPassword);

        view.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(ctEmail.getText().toString().trim(), ctPassword.getText().toString().trim() );
            }
        });
    }
    private void register( String email,String pass ) {

         if ( email.equals("") || pass.equals("") ) {
             Toast.makeText(getActivity(), "You must provide your credentials", Toast.LENGTH_SHORT).show();
             return;
         }

        firebaseAuth.createUserWithEmailAndPassword(email, pass )
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText( getActivity().getApplicationContext()  , "User created ", Toast.LENGTH_SHORT).show();

                            ctEmail.setText("");
                            ctPassword.setText("");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("", "createUserWithEmail:failure", task.getException());
                            Toast.makeText( getContext(), "Something didn't work out. Try again.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}