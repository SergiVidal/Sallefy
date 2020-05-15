package vidal.sergi.sallefyv1.controller.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vidal.sergi.sallefyv1.R;
import vidal.sergi.sallefyv1.controller.callbacks.FragmentCallback;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.model.User;
import vidal.sergi.sallefyv1.model.UserToken;
import vidal.sergi.sallefyv1.restapi.callback.UserCallback;
import vidal.sergi.sallefyv1.restapi.manager.UserManager;


public class StatsFragment extends Fragment implements UserCallback {
    public static final String TAG = StatsFragment.class.getName();
    BarChart bar;
    List<Track> topTracks;

    public static StatsFragment getInstance() {
        return new StatsFragment();
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

        View view =inflater.inflate(R.layout.fragment_stats, container, false);
        getData();
//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ProductSans.ttf");

        bar = (BarChart)view.findViewById(R.id.bar);




        return view;
    }

    private void getData(){
        topTracks = new ArrayList<>();
        UserManager.getInstance(getContext())
                .getMeTracks(this);

    }

    private void createChart(){
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, topTracks.get(0).getPlays(),topTracks.get(0).getName()));
        entries.add(new BarEntry(1f, topTracks.get(1).getPlays(),topTracks.get(1).getName()));
        entries.add(new BarEntry(2f, topTracks.get(2).getPlays(),topTracks.get(2).getName()));
        entries.add(new BarEntry(3f, topTracks.get(3).getPlays(),topTracks.get(3).getName()));
        entries.add(new BarEntry(4f, topTracks.get(4).getPlays(),topTracks.get(4).getName()));


        BarDataSet bSet = new BarDataSet(entries, "Marks");
        bSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        ArrayList<String> barFactors = new ArrayList<>();
        barFactors.add("ID: "+topTracks.get(0).getId().toString());
        barFactors.add("ID: "+topTracks.get(1).getId().toString());
        barFactors.add("ID: "+topTracks.get(2).getId().toString());
        barFactors.add("ID: "+topTracks.get(3).getId().toString());
        barFactors.add("ID: "+topTracks.get(4).getId().toString());


        XAxis xAxis = bar.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        BarData data = new BarData(bSet);
        data.setBarWidth(0.9f); // set custom bar widthe
        data.setValueTextSize(28);
        data.setValueTextColor(Color.WHITE);
        //data.set
        Description description = new Description();
        description.setTextColor(Color.WHITE);
        description.setText("All values in marks");
        bar.setDescription(description);
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
    public void onLoginSuccess(UserToken userToken) {

    }

    @Override
    public void onMeTracksSuccess(List<Track> trackList) {
        Collections.sort(trackList, (t1, t2) -> t2.getPlays().compareTo(t1.getPlays()));
        System.out.println("onMeTracksSuccess ---->");

        for (int i = 0; i < 5; i++) {
            topTracks.add(trackList.get(i));
            System.out.println(trackList.get(i));
        }
        createChart();

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

    }

    @Override
    public void onUsersFailure(Throwable throwable) {

    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
