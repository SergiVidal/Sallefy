package vidal.sergi.sallefyv1.controller.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.model.Playlist;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.PlaylistCallback;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.PlaylistManager;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;


public class StatsFollowedPlaylistsFragment extends Fragment implements PlaylistCallback {
    public static final String TAG = StatsFollowedPlaylistsFragment.class.getName();
    BarChart bar;
    List<Playlist> topPlaylists;
    private TextView tvTop1;
    private TextView tvTop2;
    private TextView tvTop3;
    private TextView tvTop4;
    private TextView tvTop5;
    View view;


    public static StatsFollowedPlaylistsFragment getInstance() {
        return new StatsFollowedPlaylistsFragment();
    }
    private FragmentCallback fragmentCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view =inflater.inflate(R.layout.fragment_stats_followed_playlists, container, false);
        getData();
//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ProductSans.ttf");

        bar = (BarChart)view.findViewById(R.id.bar);




        return view;
    }

    private void getData(){
        topPlaylists = new ArrayList<>();
        PlaylistManager.getInstance(getContext())
                .getOwnPlayList(this);

    }

    private void createChart(int size){
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> barFactors = new ArrayList<>();
        for(int i = 0; i < size; i++){
            entries.add(new BarEntry(i, topPlaylists.get(i).getFollowers(),topPlaylists.get(i).getName()));
            barFactors.add("ID: "+topPlaylists.get(i).getId().toString());
        }
        BarDataSet bSet = new BarDataSet(entries, "");
        bSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        tvTop1 = view.findViewById(R.id.tvTop1);
        tvTop2 = view.findViewById(R.id.tvTop2);
        tvTop3 = view.findViewById(R.id.tvTop3);
        tvTop4 = view.findViewById(R.id.tvTop4);
        tvTop5 = view.findViewById(R.id.tvTop5);

        switch (size){

            case 5:
                tvTop1.setText("Top1 - ID:"+topPlaylists.get(0).getId().toString()+" - "+topPlaylists.get(0).getName());
                tvTop2.setText("Top2 - ID:"+topPlaylists.get(1).getId().toString()+" - "+topPlaylists.get(1).getName());
                tvTop3.setText("Top3 - ID:"+topPlaylists.get(2).getId().toString()+" - "+topPlaylists.get(2).getName());
                tvTop4.setText("Top4 - ID:"+topPlaylists.get(3).getId().toString()+" - "+topPlaylists.get(3).getName());
                tvTop5.setText("Top5 - ID:"+topPlaylists.get(4).getId().toString()+" - "+topPlaylists.get(4).getName());
                break;
            case 4:
                tvTop1.setText("Top1 - ID:"+topPlaylists.get(0).getId().toString()+" - "+topPlaylists.get(0).getName());
                tvTop2.setText("Top2 - ID:"+topPlaylists.get(1).getId().toString()+" - "+topPlaylists.get(1).getName());
                tvTop3.setText("Top3 - ID:"+topPlaylists.get(2).getId().toString()+" - "+topPlaylists.get(2).getName());
                tvTop4.setText("Top4 - ID:"+topPlaylists.get(3).getId().toString()+" - "+topPlaylists.get(3).getName());
                break;
            case 3:
                tvTop1.setText("Top1 - ID:"+topPlaylists.get(0).getId().toString()+" - "+topPlaylists.get(0).getName());
                tvTop2.setText("Top2 - ID:"+topPlaylists.get(1).getId().toString()+" - "+topPlaylists.get(1).getName());
                tvTop3.setText("Top3 - ID:"+topPlaylists.get(2).getId().toString()+" - "+topPlaylists.get(2).getName());
                break;
            case 2:
                tvTop1.setText("Top1 - ID:"+topPlaylists.get(0).getId().toString()+" - "+topPlaylists.get(0).getName());
                tvTop2.setText("Top2 - ID:"+topPlaylists.get(1).getId().toString()+" - "+topPlaylists.get(1).getName());
                break;
            case 1:
                tvTop1.setText("Top1 - ID:"+topPlaylists.get(0).getId().toString()+" - "+topPlaylists.get(0).getName());
                break;
            default:
                tvTop1.setText("No tienes playlists disponibles");
                break;
        }

        XAxis xAxis = bar.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        BarData data = new BarData(bSet);
        data.setBarWidth(0.9f); // set custom bar widthe
        data.setValueTextSize(12);
        data.setValueTextColor(Color.WHITE);
        //data.set
        bar.setData(data);
        bar.setFitBars(true); // make the x-axis fit exactly all bars
        bar.getAxisLeft().setTextColor(Color.WHITE);
        bar.getAxisRight().setTextColor(Color.WHITE);
        bar.getXAxis().setTextColor(Color.WHITE);

        bar.invalidate(); // refresh
        bar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barFactors));


        Legend l = bar.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
//        l.setTypeface(font);
        l.setTextSize(12f);
        l.setTextColor(Color.WHITE);
//        List<LegendEntry> lentries = new ArrayList<>();
//        for (int i = 0; i < barFactors.size(); i++) {
//            LegendEntry entry = new LegendEntry();
//            entry.formColor = ColorTemplate.VORDIPLOM_COLORS[i];
//            entry.label = barFactors.get(i);
//            lentries.add(entry);
//        }
        l.setXEntrySpace(1f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(1f);
//        l.setCustom(lentries);
    }


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
    public void onAllList(ArrayList<Playlist> playlists) {

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
    public void onPersonalPlaylistReceived(ArrayList<Playlist> myPlaylists) {
        Collections.sort(myPlaylists, (t1, t2) -> t2.getFollowers().compareTo(t1.getFollowers()));
        System.out.println("onMeTracksSuccess ---->");


        if(myPlaylists.size() < 5) {
            for (int i = 0; i < myPlaylists.size(); i++) {
                topPlaylists.add(myPlaylists.get(i));
            }
            createChart(myPlaylists.size());
        }else{
            for (int i = 0; i < 5; i++) {
                topPlaylists.add(myPlaylists.get(i));
            }
            createChart(5);
        }
    }

    @Override
    public void getFollowingPlayList(ArrayList<Playlist> tracks) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
