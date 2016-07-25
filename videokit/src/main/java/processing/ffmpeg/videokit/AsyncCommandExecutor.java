package processing.ffmpeg.videokit;

import java.lang.ref.WeakReference;

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
                final int returnCode = videoKit.process(command.getAsArray());

                if (returnCode == VideoKit.FFMPEG_SUCCESS_RETURN_CODE) {
                    listener.onSuccess(command.getOutputPath());
                } else {
                    listener.onFailure(returnCode);
                }
            }
        }
    };

    AsyncCommandExecutor(Command command, VideoKit videoKit, ProcessingListener listener) {
        super(command, videoKit);

        this.listenerWeakReference = new WeakReference<>(listener);
    }

    public void execute() {
        final Thread thread = new Thread(executionRunnable);
        thread.start();
    }
}
