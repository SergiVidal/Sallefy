package vidal.sergi.sallefyv1.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search {


    private List<Playlist> playlists = null;

    private List<Track> tracks = null;

    private List<User> users = null;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public List<Playlist> getPlaylists() {
        return playlists;
    }


    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }


    public List<Track> getTracks() {
        return tracks;
    }


    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }


    public List<User> getUsers() {
        return users;
    }


    public void setUsers(List<User> users) {
        this.users = users;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
