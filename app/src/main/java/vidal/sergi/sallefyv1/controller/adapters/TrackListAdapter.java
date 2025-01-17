package vidal.sergi.sallefyv1.controller.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.TrackListCallback;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.utils.Session;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.ViewHolder> {

    private static final String TAG = "TrackListAdapter";
    private ArrayList<Track> mTracks;
    private Context mContext;
    private TrackListCallback mCallback;
    private int NUM_VIEWHOLDERS = 0;
    private String playlistAuthor;
    private int option;

    public TrackListAdapter(TrackListCallback callback, Context context, ArrayList<Track> tracks, String playlistAuthor, int option) {
        mTracks = tracks;
        mContext = context;
        mCallback = callback;
        this.playlistAuthor = playlistAuthor;
        this.option = option;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called. Num viewHolders: " + NUM_VIEWHOLDERS++);


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        ViewHolder vh = new TrackListAdapter.ViewHolder(itemView);
        Log.d(TAG, "onCreateViewHolder: called. viewHolder hashCode: " + vh.hashCode());
        return vh;
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called. viewHolder hashcode: " + holder.hashCode());


        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onTrackSelected(position, option);
            }
        });
        holder.tvTitle.setText(mTracks.get(position).getName());
        holder.tvAuthor.setText(mTracks.get(position).getUserLogin());
        if (mTracks.get(position).getThumbnail() != null) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_audiotrack)
                    .load(mTracks.get(position).getThumbnail())
                    .into(holder.ivPicture);
        }

//        TrackManager.getInstance(mContext)
//                .isLikedTrack(mTracks.get(position).getId(), this);

        holder.ibFollowTrack.setOnClickListener(v -> {
            mCallback.onLikeTrackSelected(position, option);
        });

        if (mTracks.get(position).isLiked()) {
            holder.ibFollowTrack.setBackgroundResource(R.drawable.ic_star_green);
        } else {
            holder.ibFollowTrack.setBackgroundResource(R.drawable.ic_star_border_black);

        }
        holder.ibMoreTrack.setOnClickListener(v -> {
            mCallback.onDetailsTrackSelected(position, option);
        });

        if (playlistAuthor.equals(Session.getInstance(mContext).getUser().getLogin())) {

            holder.ibDelete.setOnClickListener(v -> {
                mCallback.onDeleteTrackSelected(position);
            });
        }else{
            holder.ibDelete.setClickable(false);
            holder.ibDelete.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mTracks != null ? mTracks.size() : 0;
    }

    //    //Todo: Reutilitzar este codigo?
    public void updateTrackLikeStateIcon(int position, boolean isLiked) {
        mTracks.get(position).setLiked(isLiked);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLayout;
        TextView tvTitle;
        TextView tvAuthor;
        ImageView ivPicture;
        ImageButton ibFollowTrack;
        ImageButton ibMoreTrack;
        ImageButton ibDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.track_item_layout);
            tvTitle = (TextView) itemView.findViewById(R.id.track_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.track_author);
            ivPicture = (ImageView) itemView.findViewById(R.id.track_img);
            ibFollowTrack = itemView.findViewById(R.id.ibFollowTrack);
            ibMoreTrack = itemView.findViewById(R.id.ibMoreTrack);
            ibDelete = itemView.findViewById(R.id.ibDelete);

        }
    }
}
