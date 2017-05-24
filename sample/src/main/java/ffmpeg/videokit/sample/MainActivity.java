package ffmpeg.videokit.sample;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import processing.ffmpeg.videokit.AsyncCommandExecutor;
import processing.ffmpeg.videokit.Command;
import processing.ffmpeg.videokit.ProcessingListener;
import processing.ffmpeg.videokit.VideoKit;
import video_processing.ffmpeg.testing.R;
/**
 * Created by Ilja Kosynkin on 07.07.2016.
 * Copyright by inFullMobile
 */
public class MainActivity extends AppCompatActivity implements VideoListAdapter.Callback, ProcessingListener {
    private static final String POSTFIX = "_p.mp4";
    private static final int SPAN_COUNT = 3;
    private static final int REQUEST_CODE = 1337;

    private VideoKit videoKit = new VideoKit();

    private ProgressDialog progressDialog;
    private View rootView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rootView = findViewById(android.R.id.content);
        model = new Model(this);

        Log.d("ARCHITECTURE", System.getProperty("os.arch"));

        setupDialog();
        setupListIfWritePermissionGranted();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            setupList();
        }
    }

    private void setupDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.processing_message));
        progressDialog.setCancelable(false);
    }

    private void setupListIfWritePermissionGranted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        } else {
            setupList();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupList() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);

        final VideoListAdapter adapter = new VideoListAdapter();
        adapter.setCallback(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));

        adapter.setData(model.getVideos());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    public void onMediaFileSelected(String path) {
        progressDialog.show();

        final Command command = videoKit.createCommand()
                .overwriteOutput()
                .inputPath(path)
                .outputPath(path + POSTFIX)
                .customCommand("-ss 1 -t 3")
                .copyVideoCodec()
                .experimentalFlag()
                .build();

        new AsyncCommandExecutor(command, this).execute();
    }

    @Override
    public void onSuccess(String path) {
        progressDialog.dismiss();
        Snackbar.make(rootView, R.string.success_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(int returnCode) {
        progressDialog.dismiss();
        Snackbar.make(rootView, R.string.failure_message, Snackbar.LENGTH_LONG).show();
    }
}
