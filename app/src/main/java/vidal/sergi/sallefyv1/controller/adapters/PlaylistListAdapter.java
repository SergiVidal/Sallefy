package vidal.sergi.sallefyv1.controller.adapters;




import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.model.Playlist;

public class PlaylistListAdapter extends RecyclerView.Adapter<PlaylistListAdapter.ViewHolder> {

    private static final String TAG = "PlaylistListAdapter";
    private List<Playlist> playlistList;
    private Context mContext;

    public PlaylistListAdapter(Context context, List<Playlist> playlistList) {
        mContext = context;
        this.playlistList = playlistList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new PlaylistListAdapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        String name = "Name: " + playlistList.get(position).getName();
        String id = "ID: " + playlistList.get(position).getId();
        holder.tvPlaylistTitle.setText(name);
        holder.tvPlaylistId.setText(id);
//        holder.tvAuthor.setText(playlistList.get(position).getUserLogin());
//        if (playlistList.get(position).getThumbnail() != null) {
//            Glide.with(mContext)
//                    .asBitmap()
//                    .placeholder(R.drawable.ic_audiotrack)
//                    .load(playlistList.get(position).getThumbnail())
//                    .into(holder.ivPicture);
//        }
    }

    @Override
    public int getItemCount() {
        return playlistList != null ? playlistList.size() : 0;
    }

//    public void updateTrackLikeStateIcon(int position, boolean isLiked) {
//        mTracks.get(position).setLiked(isLiked);
//        notifyDataSetChanged();
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlaylistTitle;
        TextView tvPlaylistId;

//        TextView tvAuthor;
//        ImageView ivPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistTitle = (TextView) itemView.findViewById(R.id.playlist_title);
            tvPlaylistId = (TextView) itemView.findViewById(R.id.playlist_id);
//            tvAuthor = (TextView) itemView.findViewById(R.id.track_author);
//            ivPicture = (ImageView) itemView.findViewById(R.id.track_img);
        }
    }
}
