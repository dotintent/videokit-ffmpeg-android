package video_processing.ffmpeg.testing;

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
        videoKit = new VideoKit(context);

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
                .withListener(this)
                .processVideoAsync();
    }

    @Override
    public void onSuccess(VideoProcessingResult processingResult) {
        presentedView.showSuccessSnackbar();
    }

    @Override
    public void onFailure(VideoProcessingResult processingResult) {
        presentedView.showFailureSnackbar();
    }
}
