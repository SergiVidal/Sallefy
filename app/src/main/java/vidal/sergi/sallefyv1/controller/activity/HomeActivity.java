package vidal.sergi.sallefyv1.controller.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.adapters.GenresAdapter;
import vidal.sergi.sallefyv1.controller.adapters.PlaylistListAdapter;
import vidal.sergi.sallefyv1.controller.adapters.UserAdapter;
import vidal.sergi.sallefyv1.controller.callbacks.GenreAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.PlaylistAdapterCallback;
import vidal.sergi.sallefyv1.controller.callbacks.UserAdapterCallback;
import vidal.sergi.sallefyv1.model.Genre;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.GenreCallback;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.GenreManager;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;

public class HomeActivity extends AppCompatActivity implements UserCallback, PlaylistCallback, PlaylistAdapterCallback, GenreCallback, UserAdapterCallback, GenreAdapterCallback {

    private RecyclerView mUsersView;
    private UserAdapter mUserAdapter;

    private RecyclerView mPlaylistsView;
    private PlaylistListAdapter mPlaylistAdapter;

    private RecyclerView mGenresView;
    private GenresAdapter mGenresAdapter;

    private BottomNavigationView mNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        getData();
        System.out.println("onCreate");

    }


    @Override
    protected void onResume() {
        super.onResume();
//        mPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);
//        mPlaylistsView.setAdapter(mPlaylistAdapter);
        getData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initViews() {
        LinearLayoutManager managerUsers = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mUserAdapter = new UserAdapter(null, getApplicationContext(), this);
        mUsersView = (RecyclerView) findViewById(R.id.search_users_recyclerview);
        mUsersView.setLayoutManager(managerUsers);
        mUsersView.setAdapter(mUserAdapter);

        LinearLayoutManager managerPlaylists = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mPlaylistAdapter = new PlaylistListAdapter(null, getApplicationContext(), this, R.layout.item_playlist_short);
        mPlaylistsView = (RecyclerView) findViewById(R.id.search_playlists_recyclerview);
        mPlaylistsView.setLayoutManager(managerPlaylists);
        mPlaylistsView.setAdapter(mPlaylistAdapter);

        LinearLayoutManager managerGenres = new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);
        mGenresAdapter = new GenresAdapter(null,getApplicationContext(), this, R.layout.item_genre);
        mGenresView = (RecyclerView) findViewById(R.id.search_genres_recyclerview);
        mGenresView.setLayoutManager(managerGenres);
        mGenresView.setAdapter(mGenresAdapter);

        mNav = findViewById(R.id.bottom_navigation);
        mNav.setSelectedItemId(R.id.action_home);
        mNav.setOnNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
//                case R.id.action_home:
//                    intent = new Intent(getApplicationContext(), HomeActivity.class);
//                    startActivity(intent);
//                    break;
                case R.id.action_search:
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_library:
                    intent = new Intent(getApplicationContext(), LibraryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.action_profile:
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    break;

            }
            return true;
        });
    }

    private void getData() {
        PlaylistManager.getInstance(getApplicationContext())
                .getListOfPlaylist(this);
        UserManager.getInstance(getApplicationContext())
                .getUsers(this);
        GenreManager.getInstance(getApplicationContext())
                .getAllGenres(this);
    }


    /**********************************************************************************************
     *   *   *   *   *   *   *   *   PlaylistCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/
    @Override
    public void onCreatePlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onCreatePlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onAddTrackToPlaylistSuccess(Playlist playlist) {

    }

    @Override
    public void onAddTrackToPlaylistFailure(Throwable throwable) {

    }

    @Override
    public void onGetPlaylistReceivedSuccess(Playlist playlist) {

    }

    @Override
    public void onGetPlaylistReceivedFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistById(Playlist playlist) {

    }

    @Override
    public void onPlaylistsByUser(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onAllList(ArrayList<Playlist> playlists) {
        mPlaylistAdapter = new PlaylistListAdapter(playlists, getApplicationContext(), this, R.layout.item_playlist_short);
        mPlaylistsView.setAdapter(mPlaylistAdapter);
    }

    @Override
    public void onFollowingList(ArrayList<Playlist> playlists) {

    }

    @Override
    public void onFollowingPlaylist(Playlist playlist) {
    }

    @Override
    public void onIsFollowingPlaylist(Playlist playlist) {

    }

    @Override
    public void onNoPlaylist(Throwable throwable) {

    }

    @Override
    public void onPersonalPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onUserPlaylistReceived(ArrayList<Playlist> tracks) {

    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }

    @Override
    public void onPlaylistClick(Playlist playlist) {
        Intent intent = new Intent(getApplicationContext(), PlaylistDetailsActivity.class);
        intent.putExtra("Playlist", playlist);
        startActivity(intent);
        //startActivityForResult para recuperar
        //
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   UserCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onLoginFailure(Throwable throwable) {

    }

    @Override
    public void onRegisterSuccess() {

    }

    @Override
    public void onRegisterFailure(Throwable throwable) {

    }

    @Override
    public void onUserInfoReceived(User userData) {

    }

    @Override
    public void onUsersReceived(List<User> users) {
        mUserAdapter = new UserAdapter((ArrayList<User>) users, getApplicationContext(), this);
        mUsersView.setAdapter(mUserAdapter);
    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }

    /**********************************************************************************************
     *   *   *   *   *   *   *   *   GenreCallback   *   *   *   *   *   *   *   *   *
     **********************************************************************************************/

    @Override
    public void onGenresReceive(ArrayList<Genre> genres) {
        //ArrayList<Genre> genresString = (ArrayList<String>) genres.stream().map(Genre::getName).collect(Collectors.toList());
        mGenresAdapter = new GenresAdapter(genres,getApplicationContext(), this, R.layout.item_genre);
        mGenresView.setAdapter(mGenresAdapter);
    }

    @Override
    public void onTracksByGenre(ArrayList<Track> tracks) {

    }

    @Override
    public void onGenreClick(Genre genre) {
        Intent intent = new Intent(getApplicationContext(), GenreListActivity.class);
        intent.putExtra("Genre", genre);
        startActivity(intent);
    }
}
