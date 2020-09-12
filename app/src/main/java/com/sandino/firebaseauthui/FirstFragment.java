package com.sandino.firebaseauthui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstFragment extends Fragment {
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false );

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputLayout layoutEmail = view.findViewById(R.id.layout_email);
        TextInputLayout layoutPass = view.findViewById(R.id.layout_password);
        final TextInputEditText ctEmail = view.findViewById(R.id.ctEmail);
        final TextInputEditText ctPass = view.findViewById(R.id.ctPassword );

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        view.findViewById(R.id.lbTtitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword("juan.sandino.ba@gmail.com", "sandino420")
                        .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText( getActivity().getApplicationContext()  , "usuario creado ", Toast.LENGTH_SHORT).show();
                                    refreshUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText( getContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    refreshUI(null);
                                }

                                // ...
                            }
                        });
            }
        });
        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login( ctEmail.getText().toString().trim(), ctPass.getText().toString().trim() );
            }
        });
    }

    private void login ( String email, String pass ) {
        mAuth.signInWithEmailAndPassword( email, pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if ( task.isSuccessful() ) {
                            Toast.makeText( getActivity(), "Welcome", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            refreshUI(user);
                        } else {
                            Toast.makeText( getActivity() , "Credenciales incorrectas ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void refreshUI( FirebaseUser user) {
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Toast.makeText(getActivity(), ""+ uid, Toast.LENGTH_SHORT).show();
        }
    }

}