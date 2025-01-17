package com.example.triviaquiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
   private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView txt;
    private Button btnSignout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);

        txt=(TextView) view.findViewById(R.id.textView);
        //intialize firebase
        mAuth = FirebaseAuth.getInstance();
        btnSignout=(Button) view.findViewById(R.id.btnSignout);

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    Toast.makeText(getContext(),"onAuthStateChanged:signed_in: "+user.getEmail(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getContext(),"On auth statechanged:signed out: ",Toast.LENGTH_LONG).show();
                }
            }
        };
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent signout=new Intent(getActivity(),SplashActivity.class);
                startActivity(signout);
            }
        });

        Button btnPlay = (Button)view.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(HomeFragment.this.getContext());
                dialog.setContentView(R.layout.layout_dialog);
                dialog.show();

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Button btnStart = (Button)dialog.findViewById(R.id.btnStartQuiz);
                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent quizIntent = new Intent(HomeFragment.this.getContext(), QuizActivity.class);
                        startActivity(quizIntent);
                    }
                });
            }
        });
        return view;



    }

    //firebase current user
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void updateUI(FirebaseUser currentUser) {
        //print current user
        txt.setText(currentUser.getEmail());

    }

//   @Override
//    public  void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
//        super.onCreateOptionsMenu(menu,menuInflater);
//
//   }


}