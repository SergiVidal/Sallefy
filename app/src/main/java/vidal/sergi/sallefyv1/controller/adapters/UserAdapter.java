package vidal.sergi.sallefyv1.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public static final String TAG = UserAdapter.class.getName();

    private ArrayList<User> mUsers;
    private Context mContext;
    private UserAdapterCallback mCallback;

    public UserAdapter(ArrayList<User> users, Context context, UserAdapterCallback callback) {
        mUsers = users;
        mContext = context;
        mCallback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mUsers != null && mUsers.size() > 0) {
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null)
                        mCallback.onUserClick(mUsers.get(position));
                }
            });
            holder.tvUsername.setText(mUsers.get(position).getLogin());

//        if (mUsers.get(position).getImageUrl() != null && !mUsers.get(position).getImageUrl().equals("")) {
            Glide.with(mContext)
                    .asBitmap()
                    .placeholder(R.drawable.ic_account_circle_black)
                    .load(mUsers.get(position).getImageUrl())
                    .into(holder.ivPhoto);
//        }
        }
    }

    @Override
    public int getItemCount() {
        return (mUsers != null ? mUsers.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mLayout;
        TextView tvUsername;
        ImageView ivPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = (ConstraintLayout) itemView.findViewById(R.id.item_user_layout);
            tvUsername = (TextView) itemView.findViewById(R.id.item_user_name);
            ivPhoto = (ImageView) itemView.findViewById(R.id.item_user_photo);
        }
    }
}
