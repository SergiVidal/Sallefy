package vidal.sergi.sallefyv1.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Track implements Serializable {

    @SerializedName("color")
    private String color;

    @SerializedName("duration")
    private Integer duration;

    @SerializedName("genres")
    private List<Genre> genres = null;

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("owner")
    private User user;

    @SerializedName("released")
    private String released;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("url")
    private String url;

    @SerializedName("plays")
    private Integer plays;

    @SerializedName("likes")
    private Integer likes;

    private boolean selected = false;

    private boolean liked;

    public Track() {
    }

    public Track(Integer id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
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
        user.setLogin(userLogin);
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getPlays() {
        return plays;
    }

    public void setPlays(Integer plays) {
        this.plays = plays;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "Track{" +
                "color='" + color + '\'' +
                ", duration=" + duration +
                ", genres=" + genres +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                ", released='" + released + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", url='" + url + '\'' +
                ", plays=" + plays +
                ", likes=" + likes +
                ", selected=" + selected +
                ", liked=" + liked +
                '}';
    }
}
