package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tujue.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import handler.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private Context context;
    private List<Article> articles;


    public ArticleAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.title.setText(article.getTitle());
        holder.author.setText(article.getAuthor());
        holder.desc.setText(article.getDescription());
        Picasso.get()
                .load(article.getUrlToImage())
                .fit()
                .error(R.drawable.ic_error_outline_white_24dp)
                .into(holder.icon, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Tujue", "imgLoad:success");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.i("Tujue", "imgLoad:failure", e);
                    }

                });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, desc;
        public CircleImageView icon;

        public ViewHolder(View view) {
            super(view);
            title =  view.findViewById(R.id.title_article);
            author =  view.findViewById(R.id.author_article);
            desc =  view.findViewById(R.id.desc_article);
            icon = view.findViewById(R.id.img_article);
        }
    }
}
