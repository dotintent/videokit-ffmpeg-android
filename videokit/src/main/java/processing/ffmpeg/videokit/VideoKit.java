package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 * Copyright by inFullMobile
 */
public class VideoKit {
    public static final int FFMPEG_SUCCESS_RETURN_CODE = 0;

    static {
        System.loadLibrary("avutil-54");
        System.loadLibrary("swresample-1");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avformat-56");
        System.loadLibrary("swscale-3");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("videokit");
    }

    private LogLevel logLevel = LogLevel.NO_LOG;

    public void  setLogLevel(LogLevel level) {
        logLevel = level;
    }

    int process(String[] args) {
        return run(logLevel.getValue(), args);
    }

    @SuppressWarnings("JniMissingFunction")
    private native int run(int loglevel, String[] args);

    public ICommandBuilder buildCommand() {
        return new CommandBuilder(this);
    }

    public SyncCommandExecutor createSyncExecutorWithCommand(Command command) {
        return new SyncCommandExecutor(command, this);
    }

    public AsyncCommandExecutor createAsyncExecutorWithCommand(Command command,
                                                               ProcessingListener listener) {
        return new AsyncCommandExecutor(command, this, listener);
    }
}
