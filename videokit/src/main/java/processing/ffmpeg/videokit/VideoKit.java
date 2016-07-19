package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 * Copyright by inFullMobile
 */
public class VideoKit {
    public static final int FFMPEG_SUCCESS_RETURN_CODE = 0;
    private static VideoKit instance;

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

    public static VideoKit getInstance() {
        return instance;
    }

    private LogLevel logLevel = LogLevel.NO_LOG;

    public VideoKit() {
        if (instance != null) {
            throw new RuntimeException("VideoKit already initialized. " +
                    "Use getInstance instead of constructor");
        }

        instance = this;
    }

    public void  setLogLevel(LogLevel level) {
        logLevel = level;
    }

    public int process(String[] args) {
        return run(logLevel.getValue(), args);
    }

    private native int run(int loglevel, String[] args);

    public ICommandBuilder buildCommand() {
        return new CommandBuilder();
    }
}
