package vidal.sergi.sallefyv1.controller.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.utils.Session;

public class ProfileFragment extends Fragment {
    private TextView tvName;
    private TextView tvMail;
    private TextView tvFollowers;
    private TextView tvFollowings;
    public static final String TAG = ProfileFragment.class.getName();

    public static ProfileFragment getInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(v);
        return v;
    }
    private void initViews(View v) {
        tvName = v.findViewById(R.id.name);
        tvName.setText(Session.getInstance(getContext()).getUser().getLogin());

        tvMail = v.findViewById(R.id.mail);
        tvMail.setText(Session.getInstance(getContext()).getUser().getEmail());

        tvFollowers = v.findViewById(R.id.followers);
        tvFollowers.setText("Followers: "+Session.getInstance(getContext()).getUser().getFollowers());

        tvFollowings = v.findViewById(R.id.followings);
        tvFollowings.setText("Followings: "+Session.getInstance(getContext()).getUser().getFollowing());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
