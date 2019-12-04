package fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tujue.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import handler.Article;
import handler.BuilderAPI;
import handler.Constants;
import handler.InterfaceRequest;
import handler.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopHead extends Fragment {


    public TopHead() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        getTopArticles();
        return view;
    }

    private void getTopArticles() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        BuilderAPI.apiService.getHeadlines().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.body() != null){
                    Toasty.success(getContext(),"Successful",
                            Toast.LENGTH_SHORT,true).show();
                    Log.d(Constants.TAG, response.body().getStatus());
                } else {
                    Toasty.warning(getContext(),"No articles found",Toast.LENGTH_SHORT,true).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(getContext(),"Connection Error. Please ensure you're connected to the internet",
                        Toast.LENGTH_SHORT,true).show();
            }
        });
    }

}
