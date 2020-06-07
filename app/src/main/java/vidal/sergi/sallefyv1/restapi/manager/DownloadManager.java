package vidal.sergi.sallefyv1.restapi.manager;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLOutput;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vidal.sergi.sallefyv1.model.Database;
import vidal.sergi.sallefyv1.model.Track;
import vidal.sergi.sallefyv1.restapi.callback.DownloadCallback;
import vidal.sergi.sallefyv1.restapi.service.UserService;
import vidal.sergi.sallefyv1.restapi.service.UserTokenService;
import vidal.sergi.sallefyv1.utils.Constants;

public class DownloadManager {

    private static final String TAG = "DownloadManager";
    private static DownloadManager downloadManager;
    private Retrofit mRetrofit;
    private Context mContext;

//    private Request request;

//    private UserService mService;
//    private UserTokenService mTokenService;

    public static DownloadManager getInstance(Context context) {
        if (downloadManager == null) {
            downloadManager = new DownloadManager(context);
        }
        return downloadManager;
    }

    private DownloadManager(Context cntxt) {
        mContext = cntxt;
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(Constants.NETWORK.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

//        mService = mRetrofit.create(UserService.class);
//        mTokenService = mRetrofit.create(UserTokenService.class);
    }

    public void downloadTrack(Track track, final DownloadCallback downloadCallback) {
        OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(track.getUrl())
                    .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                        try {
//                            final File path =
//                                    Environment.getExternalStoragePublicDirectory
//                                            (
//                                                    //Environment.DIRECTORY_PICTURES
//                                                    Environment.DIRECTORY_DCIM + "/Sallefy/"
//                                            );
//
//                            // Make sure the path directory exists.
//                            if(!path.exists())
//                            {
//                                // Make it, if it doesn't exit
//                                path.mkdirs();
//                            }
//
//                            final File file = new File(path, filename);
//                            try
//                            {
//                                file.createNewFile();
//                                FileOutputStream fOut = new FileOutputStream(file);
//                                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//                                myOutWriter.append(response.body().string());
//
//                                myOutWriter.close();
//
//                                fOut.flush();
//                                fOut.close();
//                                downloadCallback.onSongDownload(track);
//
//                            }
//                            catch (IOException e)
//                            {
//                                Log.e("Exception", "File write failed: " + e.toString());
//                            }

                            String myDir = Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_MUSIC).toString();
                            System.out.println("test ----->" + myDir);

                            String filename = "/"+ track.getId() + ".mp3";
                            File file = new File(myDir, filename);

                            if (!file.exists()) {
                                file.createNewFile();
                                Log.e(TAG, "File Created");
                            }
                            System.out.println("rESPONSE --->>>"+response.body().byteStream());

                            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                            fileOutputStream.write(response.body().bytes());
                            fileOutputStream.close();

//                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput(filename, Context.MODE_PRIVATE));
//                            outputStreamWriter.write(response.body().byteStream().toString());
//                            outputStreamWriter.close();



                            downloadCallback.onSongDownload(new Database(track.getId(), myDir+filename, track.getName(), track.getThumbnail()));
                            System.out.println("test ---> : " + Environment.getExternalStorageDirectory());

                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }

                }
            }

        });
    }
}


