package fragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tujue.R;

import java.util.ArrayList;
import java.util.List;

import adapters.ArticleAdapter;
import es.dmoral.toasty.Toasty;
import handler.Article;
import handler.BuilderAPI;
import handler.Constants;
import handler.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Health extends Fragment {
    private RecyclerView health;
    private List<Article> articles = new ArrayList<>();
    private ArticleAdapter article_adapter;


    public Health() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        health = view.findViewById(R.id.recycler_health);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        health.setLayoutManager(layoutManager);
        health.setHasFixedSize(true);


        //Get Articles
        getTopArticles();

        article_adapter = new ArticleAdapter(getContext(),articles);
        health.setAdapter(article_adapter);

        return view;
    }

    private void getTopArticles() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Health");
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        BuilderAPI.apiService.getHealth().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                if (response.body() != null){
                    Toasty.success(getContext(),"Successful",
                            Toast.LENGTH_SHORT,true).show();
                    //Assign results to Recyclerview
                    article_adapter = new ArticleAdapter(getContext(), response.body().getArticles());
                    Log.d("Tujue", article_adapter.toString());
                    health.setAdapter(article_adapter);

                    Log.d(Constants.TAG, response.body().getArticles().toString());
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
