package processing.ffmpeg.videokit;

import java.lang.ref.WeakReference;

/**
 * Created by Ilja Kosynkin on 19.07.16.
 * Copyright by inFullMobile
 */
public class AsyncCommandExecutor {
    private final WeakReference<ProcessingListener> listenerWeakReference;
    private final Command command;

    private final Runnable executionRunnable = new Runnable() {
        @Override
        public void run() {
            final VideoProcessingResult result = command.execute();

            final ProcessingListener listener = listenerWeakReference.get();
            if (listener != null) {
                if (result.isSuccessful()) {
                    listener.onSuccess(result.getPath());
                } else {
                    listener.onFailure(result.getCode());
                }
            }
        }
    };

    public AsyncCommandExecutor(Command command, ProcessingListener listener) {
        this.command = command;
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    public void execute() {
        final Thread thread = new Thread(executionRunnable);
        thread.start();
    }
}
