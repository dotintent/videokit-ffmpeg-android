package processing.ffmpeg.videokit;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
public class AsyncCommandExecutor extends AbstractCommandExecutor {
    private final WeakReference<ProcessingListener> listenerWeakReference;

    private final Runnable executionRunnable = new Runnable() {
        @Override
        public void run() {
            final ProcessingListener listener = listenerWeakReference.get();
            if (listener != null) {
                final int returnCode = VideoKit.getInstance().process(flagsToArgs());
                processResultCode(returnCode);

                if (returnCode == VideoKit.FFMPEG_SUCCESS_RETURN_CODE) {
                    listener.onSuccess(outputPath);
                } else {
                    listener.onFailure(returnCode);
                }
            }
        }
    };

    AsyncCommandExecutor(String outputPath, String inputPath, boolean deleteInput,
                         List<String> flags, ProcessingListener listenerWeakReference) {
        super(outputPath, inputPath, deleteInput, flags);

        this.listenerWeakReference = new WeakReference<>(listenerWeakReference);
    }

    public void execute() {
        final Thread thread = new Thread(executionRunnable);
        thread.start();
    }
}
