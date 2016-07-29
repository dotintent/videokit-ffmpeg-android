package ffmpeg.videokit.sample;

import android.content.Context;

import processing.ffmpeg.videokit.AsyncCommandExecutor;
import processing.ffmpeg.videokit.Command;
import processing.ffmpeg.videokit.ProcessingListener;
import processing.ffmpeg.videokit.VideoKit;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
 * Copyright by inFullMobile
 */
public class Presenter implements VideosAdapter.Callback, ProcessingListener {
    private static final String POSTFIX = "_p.mp4";

    private final Model model;
    private final VideoKit videoKit;
    private final PresentedView presentedView;

    public Presenter(Context context, PresentedView view) {
        presentedView = view;
        model = new Model(context);
        videoKit = new VideoKit();

        loadData();
    }

    private void loadData() {
        presentedView.presentData(model.getVideos());
    }

    @Override
    public void onMediaFileSelected(String path) {
        presentedView.showSpinner();

        final Command command = videoKit.createCommand()
                .overwriteOutput()
                .addInputPath(path)
                .addOutputPath(path + POSTFIX)
                .addCustomCommand("-ss 1 -t 3")
                .copyVideoCodec()
                .addExperimentalFlag()
                .build();

        new AsyncCommandExecutor(command, this).execute();
    }

    @Override
    public void onSuccess(String path) {
        presentedView.showSuccessSnackbar();
    }

    @Override
    public void onFailure(int returnCode) {
        presentedView.showFailureSnackbar();
    }
}
