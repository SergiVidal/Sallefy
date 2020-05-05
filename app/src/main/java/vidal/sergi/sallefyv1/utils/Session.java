package vidal.sergi.sallefyv1.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserRegister;
import vidal.sergi.sallefyv1.model.UserToken;

public class Session {
    public static Session sSession;
    private static Object mutex = new Object();

    private static Context mContext;

    private UserRegister mUserRegister;
    private User mUser;
    private UserToken mUserToken;

    private List<Playlist> playlistList;

    private boolean audioEnabled;
    private Track mTrack;
    private Playlist mPlaylist;
    private ArrayList<Track> mTracks;
    private int mIndex;
    private boolean isPlaying;

    public static Session getInstance(Context context) {
        Session result = sSession;
        if (result == null) {
            synchronized (mutex) {
                result = sSession;
                if (result == null)
                    sSession = result = new Session(context);
            }
        }
        return result;
    }

    private Session() {}

    public Session(Context context) {
        this.mContext = context;
        this.mUserRegister = null;
        this.mUser = null;
        this.mUserToken = null;
        this.playlistList = new ArrayList<>();
        this.audioEnabled = false;
        this.isPlaying = false;
    }

    public void resetValues() {
        mUserRegister = null;
        mUserToken = null;
        mUser = null;
        mTrack = null;
        mPlaylist = null;
        mIndex = -1;
        isPlaying = false;
    }

    public static Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public UserRegister getUserRegister() {
        return mUserRegister;
    }

    public void setUserRegister(UserRegister userRegister) {
        mUserRegister = userRegister;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public UserToken getUserToken() {
        return mUserToken;
    }

    public void setUserToken(UserToken userToken) {
        this.mUserToken = userToken;
    }

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setplaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
    }

    public Track getTrack() {
        return mTrack;
    }

    public void setTrack(Track track) {
        mTrack = track;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public void setTrack(ArrayList<Track> tracks, int index) {
        mTracks = tracks;
        mIndex = index;
        mTrack = tracks.get(index);
    }

    public void setTrack(Playlist playlist, int index) {
        mTracks = (ArrayList<Track>) playlist.getTracks();
        mIndex = index;
        mTrack = mTracks.get(index);
    }

    public Playlist getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(Playlist playlist) {
        mPlaylist = playlist;
    }

    public ArrayList<Track> getTracks() {
        return mTracks;
    }

    public void setTracks(ArrayList<Track> tracks) {
        mTracks = tracks;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}

