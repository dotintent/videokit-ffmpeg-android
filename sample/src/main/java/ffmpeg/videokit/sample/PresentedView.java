package ffmpeg.videokit.sample;

import java.util.List;

/**
 * Created by Ilja Kosynkin on 07.07.2016.
 */
public interface PresentedView {
    void presentData(List<VideoFile> videos);

    void showSuccessSnackbar();

    void showFailureSnackbar();

    void showSpinner();
}
