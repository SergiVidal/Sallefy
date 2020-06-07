package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.controller.dialogs.StateDialog;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.GenreCallback;
import vidal.sergi.sallefyv1.restapi.callback.TrackCallback;
import vidal.sergi.sallefyv1.restapi.manager.CloudinaryManager;
import vidal.sergi.sallefyv1.restapi.manager.GenreManager;
import vidal.sergi.sallefyv1.utils.Constants;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment implements GenreCallback, TrackCallback {

    private EditText etTitle;
    private Spinner mSpinner;
    private TextView mFilename;
    private Button btnFind, btnCancel, btnAccept;

    private ArrayList<String> mGenres;
    private ArrayList<Genre> mGenresObjs;
    private Uri mFileUri;

    private FragmentCallback fragmentCallback;

    private Context mContext;

    public static UploadFragment getInstance() {
        return new UploadFragment();
    }

    public static final String TAG = UploadFragment.class.getName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        View v = inflater.inflate(R.layout.fragment_create_song, container, false);
        initViews(v);
        return v;

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentCallback = (FragmentCallback) context;
    }
    private void initViews(View v) {
        etTitle = v.findViewById(R.id.create_song_title);
        mFilename = v.findViewById(R.id.create_song_file_name);

        mSpinner = v.findViewById(R.id.create_song_genre);

        btnFind = v.findViewById(R.id.create_song_file);
        btnFind.setOnClickListener(v1 -> getAudioFromStorage());

        btnAccept = v.findViewById(R.id.create_song_accept);
        btnAccept.setOnClickListener(v1 -> {
            if (checkParameters()) {
                etTitle.setFocusable(false);
                showStateDialog(false);
                uploadToCloudinary();
            }
        });

    }

    private void getData() {
        GenreManager.getInstance(getContext()).getAllGenres(this);
    }

    private boolean checkParameters() {
        if (!etTitle.getText().toString().equals("")) {
            if (mFileUri != null) {
                return true;
            }
        }
        return false;
    }

    private void showStateDialog(boolean completed) {
        StateDialog.getInstance(getContext()).showStateDialog(completed);
    }

    private void getAudioFromStorage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(Intent.createChooser(intent, "Choose a song"), Constants.STORAGE.SONG_SELECTED);
    }

    private void uploadToCloudinary() {
        Genre genre = new Genre();
        for (Genre g: mGenresObjs) {
            if (g.getName().equals(mSpinner.getSelectedItem().toString())) {
                genre = g;
            }
        }
        CloudinaryManager.getInstance(getContext(), this).uploadAudioFile(mFileUri, etTitle.getText().toString(), genre);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STORAGE.SONG_SELECTED && resultCode == RESULT_OK) {
            mFileUri = data.getData();
            mFilename.setText(mFileUri.toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   GenreCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {
        mGenresObjs = genres;
        mGenres = (ArrayList<String>) genres.stream().map(Genre -> Genre.getName()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, mGenres);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   TrackCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onNoTracks(Throwable throwable) {

    }

    @Override
    public void onPersonalTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onUserTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onLikedTrack(Track track) {

    }

    @Override
    public void onIsLikedTrack(Track track) {

    }

    @Override
    public void onCreateTrack() {
        StateDialog.getInstance(getContext()).showStateDialog(true);
        Thread watchDialog = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (StateDialog.getInstance(mContext).isDialogShown()){}
                } catch (Exception e) {
                }
            }
        });
        watchDialog.start();
    }

    @Override
    public void onLikedTracksReceived(List<Track> tracks) {

    }

    @Override
    public void onPlayedTrack(Track track) {

    }

    @Override
    public void onSharedTrack(Track track) {

    }
}
