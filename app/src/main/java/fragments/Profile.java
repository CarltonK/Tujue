package fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tujue.MainActivity;
import com.example.tujue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import handler.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private TextView email_view, uid_view, chG_pass, delete_account;
    private FirebaseAuth app_auth;
    private FirebaseUser current_user;
    private EditText edit_email_chg;

    //Identifiers
    String email, uid, email_pass_reset;

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
        edit_email_chg = view.findViewById(R.id.edt_chg_password);
        chG_pass = view.findViewById(R.id.chg_pass);
        delete_account = view.findViewById(R.id.delete_acc);

        email_view.setText("Email: " + email);
        uid_view.setText("Unique Id: " + uid);


        chG_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempt_reset();
            }
        });

        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toasty.info(getContext(),"Account Deleted Successfully",
                                            Toast.LENGTH_LONG, true).show();
                                    Log.d(Constants.TAG, "User account deleted.");
                                } else {
                                    Log.d(Constants.TAG, "UseraccDeleted:failure", task.getException());
                                    Toasty.error(getContext(),"Error",
                                            Toast.LENGTH_LONG, true).show();
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void attempt_reset() {
        email_pass_reset = edit_email_chg.getText().toString();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email_pass_reset)) {
            edit_email_chg.setError("Email is required");
            focusView = edit_email_chg;
            cancel = true;

        }

        if (cancel) {
            focusView.requestFocus();
        }
        else{

            if (!isOnline(getContext())) {
                Toasty.warning(getContext(),"NO INTERNET CONNECTION",
                        Toast.LENGTH_LONG, true).show();
            } else {
                reset_process(email_pass_reset);

            }
        }
    }

    private void reset_process(String email_pass_reset) {
        current_user.updatePassword(email_pass_reset)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(getContext(),"Password updated",
                                    Toast.LENGTH_LONG, true).show();
                            Log.d(Constants.TAG, "User password updated.");
                        } else {
                            Log.d(Constants.TAG, "passwordUpdate:failure" , task.getException());
                            Toasty.error(getContext(),"Error",
                                    Toast.LENGTH_LONG, true).show();
                        }
                    }
                });
    }

    private boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
