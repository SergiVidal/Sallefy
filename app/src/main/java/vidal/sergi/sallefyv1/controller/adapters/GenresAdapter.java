package vidal.sergi.sallefyv1.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.GenreAdapterCallback;
import vidal.sergi.sallefyv1.model.Genre;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {

    public static final String TAG = GenresAdapter.class.getName();
    private GenreAdapterCallback mCallback;
    private Context mContext;
    private ArrayList<Genre> mGenres;
    private int layoutId;

    public GenresAdapter(ArrayList<Genre> genres, Context context, GenreAdapterCallback callback, int layoutId) {
        mGenres = genres;
        mContext = context;
        mCallback = callback;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new GenresAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(mGenres.get(position).getName());
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null)
                    mCallback.onGenreClick(mGenres.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mGenres != null ? mGenres.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        Button tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.item_genre_layout);
            tvName = (Button) itemView.findViewById(R.id.item_genre_btn);
        }
    }
}
