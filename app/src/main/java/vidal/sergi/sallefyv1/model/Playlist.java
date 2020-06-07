package vidal.sergi.sallefyv1.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.io.Serializable;

public class Playlist implements Serializable {

    @SerializedName("cover")
    private String cover;

    @SerializedName("description")
    private String description;

    @SerializedName("followers")
    private Integer followers;

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("public")
    private Boolean publicAccessible;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("owner")
    private User user;

    @SerializedName("tracks")
    private List<Track> tracks = null;

    private boolean followed;

    public Playlist() {
    }

    public Playlist(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPublicAccessible() {
        return publicAccessible;
    }

    public void setPublicAccessible(Boolean publicAccessible) {
        this.publicAccessible = publicAccessible;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserLogin() {
        return user.getLogin();
    }

    public void setUserLogin(String userLogin) {
        if (user == null) { user = new User(); }
        user.setLogin(userLogin);
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", publicAccessible=" + publicAccessible +
                ", thumbnail='" + thumbnail + '\'' +
                ", user=" + user +
                ", tracks=" + tracks +
                ", followed=" + followed +
                '}';
    }
}
