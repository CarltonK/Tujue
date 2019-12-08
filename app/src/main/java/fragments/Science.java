package fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.tujue.WebReadActivity;

import java.util.ArrayList;
import java.util.List;

import adapters.ArticleAdapter;
import es.dmoral.toasty.Toasty;
import handler.Article;
import handler.BuilderAPI;
import handler.Constants;
import handler.DatabaseLocal;
import handler.RecyclerItemClickListener;
import handler.ServerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Science extends Fragment {
    private RecyclerView science;
    private List<Article> articles = new ArrayList<>();
    private ArticleAdapter article_adapter;
    private DatabaseLocal local_db;


    public Science() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_science, container, false);

        science = view.findViewById(R.id.recycler_science);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        science.setLayoutManager(layoutManager);
        science.setHasFixedSize(true);


        local_db = new DatabaseLocal(getContext());
        //Get Articles
        getTopArticles();

        article_adapter = new ArticleAdapter(getContext(),articles);
        science.setAdapter(article_adapter);
        return view;
    }

    private void getTopArticles() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Science");
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        BuilderAPI.apiService.getScience().enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, final Response<ServerResponse> response) {

                if (response.body() != null){
                    //Assign results to Recyclerview
                    article_adapter = new ArticleAdapter(getContext(), response.body().getArticles());
                    Log.d("Tujue", article_adapter.toString());
                    science.setAdapter(article_adapter);

                    science.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {


                                    local_db.saveArticle(response.body().getArticles().get(position).getSource().getName(),
                                            response.body().getArticles().get(position).getAuthor(),
                                            response.body().getArticles().get(position).getTitle(),
                                            response.body().getArticles().get(position).getContent(),
                                            response.body().getArticles().get(position).getDescription(),
                                            response.body().getArticles().get(position).getUrl(),
                                            response.body().getArticles().get(position).getUrlToImage(),
                                            response.body().getArticles().get(position).getPublishedAt());

                                    startActivity(new Intent(getContext(), WebReadActivity.class));

                                }
                            }));
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
