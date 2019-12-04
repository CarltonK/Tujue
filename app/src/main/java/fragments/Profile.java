package fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tujue.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private TextView email_view, uid_view;
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;

    //Identifiers
    String email, uid;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        app_auth = FirebaseAuth.getInstance();
        current_user = app_auth.getCurrentUser();

        email = current_user.getEmail();
        uid = current_user.getUid();

        email_view = view.findViewById(R.id.user_email);
        uid_view = view.findViewById(R.id.user_uid);

        email_view.setText("Email: " + email);
        uid_view.setText("Unique Id: " + uid);

        return view;
    }

}
