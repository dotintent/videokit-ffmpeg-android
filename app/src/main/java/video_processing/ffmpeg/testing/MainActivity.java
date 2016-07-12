package video_processing.ffmpeg.testing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PresentedView  {
    private static final int SPAN_COUNT = 3;

    private ProgressDialog progressDialog;
    private VideosAdapter adapter;
    private Presenter presenter;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootView = findViewById(android.R.id.content);
        setupDialog();
        setupList();
    }

    private void setupDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.processing_message));
        progressDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter = new Presenter(this, this);
        adapter.setCallback(presenter);
    }

    private void setupList() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        adapter = new VideosAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, SPAN_COUNT));
    }

    @Override
    public void presentData(List<VideoFile> videos) {
        adapter.setData(videos);
    }

    @Override
    public void showSuccessSnackbar() {
        progressDialog.dismiss();
        Snackbar.make(rootView, R.string.success_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showFailureSnackbar() {
        progressDialog.dismiss();
        Snackbar.make(rootView, R.string.failure_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSpinner() {
        progressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
