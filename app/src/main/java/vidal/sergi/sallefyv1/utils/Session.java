package vidal.sergi.sallefyv1.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserRegister;
import vidal.sergi.sallefyv1.model.UserToken;

public class Session {
    public static Session sSession;
    private static Object mutex = new Object();

    private Context mContext;

    private UserRegister mUserRegister;
    private User mUser;
    private UserToken mUserToken;
    private List<Playlist> playlistList;

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
    }

    public void resetValues() {
        mUserRegister = null;
        mUserToken = null;
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
}

