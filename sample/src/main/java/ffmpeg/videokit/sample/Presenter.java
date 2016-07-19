package ffmpeg.videokit.sample;

import android.content.Context;

import processing.ffmpeg.videokit.ProcessingListener;
import processing.ffmpeg.videokit.VideoKit;
import processing.ffmpeg.videokit.VideoProcessingResult;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
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
        videoKit.buildCommand()
                .overwriteOutput()
                .addInputPath(path)
                .addOutputPath(path + POSTFIX)
                .addCustomCommand("-ss 1 -t 3")
                .copyVideoCodec()
                .addExperimentalFlag()
                .executeCommandAsync(this);
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
