package processing.ffmpeg.videokit;

import android.content.Context;
import android.media.MediaMetadataRetriever;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilja Kosynkin on 06.07.2016.
 */
public class VideoKit {
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

    private static final int SUCCESS_CODE = 0;
    private static final MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

    private final Context context;
    private LogLevel logLevel = LogLevel.NO_LOG;

    public VideoKit(Context context) {
        this.context = context.getApplicationContext();
    }

    public void  setLogLevel(LogLevel level) {
        logLevel = level;
    }

    /**
     * Call FFmpeg with specified arguments
     * @param args FFmpeg arguments
     * @return ret_code equal to 0 if success, for handled codes see file ffmpeg_ret_codes in docs
     */
    private int process(String[] args) {
        final String[] params = new String[args.length + 1];
        params[0] = "ffmpeg";
        System.arraycopy(args, 0, params, 1, args.length);

        return run(logLevel.getValue(), params);
    }

    /**
     * Native call of ffmpeg's main
     * @param loglevel if 0 there is no log from native code, if 1 - full log of ffmpeg process
     * @param args arguments to pass to ffmpeg's main, basically command-line flags
     * @return code with which ffmpeg have exited
     */
    private native int run(int loglevel, String[] args);

    public CommandBuilder buildCommand() {
        return new CommandBuilder();
    }

    public class CommandBuilder {
        private static final String OVERWRITE_FLAG = "-y";
        private static final String INPUT_FILE_FLAG = "-i";
        private static final String TRIM_FLAG = "-ss";
        private static final String DURATION_FLAG = "-t";
        private static final String COPY_FLAG = "copy";
        private static final String VIDEO_CODEC_FLAG = "-vcodec";
        private static final String AUDIO_CODEC_FLAG = "-acodec";
        private static final String VIDEO_FILTER_FLAG = "-vf";
        private static final String VIDEO_BITRATE_FLAG = "-b:v";
        private static final String STRICT_FLAG = "-strict";
        private static final String EXPERIMENTAL_FLAG = "-2";
        private static final String THREADS_FLAG = "-threads";
        private static final String THREADS_NUMBER = "5";
        private static final String REMOVE_AUDIO_STREAM_FLAG = "-an";
        private static final String FRAME_RATE_FLAG = "-framerate";
        private static final String AUTO_FLAG = "auto";
        private static final String TUNE_FLAG = "-tune";
        private static final String FAST_DECODE = "fastdecode";
        private static final String ZERO_LATENCY = "zerolatency";

        protected final List<String> flags = new ArrayList<>();

        protected String inputPath;
        protected String outputPath;
        protected boolean deleteInput;

        private CommandBuilder() {

        }

        public CommandBuilder overwriteOutput() {
            flags.add(OVERWRITE_FLAG);
            return this;
        }

        public CommandBuilder addInputPath(String inputFilePath) {
            this.inputPath = inputFilePath;
            flags.add(INPUT_FILE_FLAG);
            flags.add(inputFilePath);
            return this;
        }

        public CommandBuilder addOutputPath(String outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public CommandBuilder trimForDuration(int startPosition, int duration) {
            flags.add(TRIM_FLAG);
            flags.add(String.valueOf(startPosition));
            flags.add(DURATION_FLAG);
            flags.add(String.valueOf(duration));
            return this;
        }

        public CommandBuilder withoutAudio() {
            flags.add(REMOVE_AUDIO_STREAM_FLAG);
            return this;
        }

        public CommandBuilder copyVideoCodec() {
            flags.add(VIDEO_CODEC_FLAG);
            flags.add(COPY_FLAG);
            return this;
        }

        public CommandBuilder addCrop(int x, int y, int width, int height) {
            flags.add(VIDEO_FILTER_FLAG);
            flags.add("crop=" + width + ":" + height + ":" + x + ":" + y);
            return this;
        }

        public CommandBuilder addCustomCommand(String customCommand) {
            final String[] splitedCommand = customCommand.trim().split(" ");

            for (String command : splitedCommand) {
                flags.add(command);
            }

            return this;
        }

        public CommandBuilder limitVideoBitrate(String bitrate) {
            flags.add(VIDEO_BITRATE_FLAG);
            flags.add(bitrate);
            return this;
        }

        public CommandBuilder addExperimentalFlag() {
            flags.add(STRICT_FLAG);
            flags.add(EXPERIMENTAL_FLAG);
            return this;
        }

        public CommandBuilder deleteInput(boolean delete) {
            deleteInput = delete;
            return this;
        }

        public CommandBuilder setAutoThreadingFlag() {
            flags.add(THREADS_FLAG);
            flags.add(THREADS_NUMBER);
            return this;
        }

        public CommandBuilder limitFrameRate(int framerate) {
            flags.add(FRAME_RATE_FLAG);
            flags.add(String.valueOf(framerate));
            return this;
        }

        public CommandBuilder setTuneToFast() {
            flags.add(TUNE_FLAG);
            flags.add(FAST_DECODE);
            flags.add(TUNE_FLAG);
            flags.add(ZERO_LATENCY);
            return this;
        }

        public AsyncCommandBuilder withListener(ProcessingListener listener) {
            return new AsyncCommandBuilder(this, listener);
        }

        public VideoProcessingResult processVideo() {
            checkInputAndOutput();
            flags.add(outputPath);

            final String ffmpegArguments[] = convertFlagsToArray();
            final int returnCode = process(ffmpegArguments);
            if (returnCode == 0) {
                deleteInputFileIfNecessary();
                return new VideoProcessingResult(returnCode, outputPath);
            } else {
                return new VideoProcessingResult(returnCode, inputPath);
            }
        }

        protected void checkInputAndOutput() {
            if (inputPath == null || !(new File(inputPath).exists())) {
                throw new RuntimeException("Specify correct path to existing video file!");
            }

            if (outputPath == null) {
                throw new RuntimeException("What I'm supposed to do with not-existing output path?");
            }
        }

        protected String[] convertFlagsToArray() {
            final String ffmpegArguments[] = new String[flags.size()];
            for (int i = 0; i < flags.size(); i++) {
                ffmpegArguments[i] = flags.get(i);
            }
            return ffmpegArguments;
        }

        protected void deleteInputFileIfNecessary() {
            if (deleteInput) {
                final File inputFile = new File(inputPath);
                if (inputFile.exists()) {
                    inputFile.delete();
                }
            }
        }
    }

    public class AsyncCommandBuilder extends CommandBuilder implements Runnable {
        private final WeakReference<ProcessingListener> processingListener;
        private final CommandBuilder builderParent;

        AsyncCommandBuilder(CommandBuilder parent, ProcessingListener listener) {
            processingListener = new WeakReference<ProcessingListener>(listener);
            builderParent = parent;
        }

        @Override
        public VideoProcessingResult processVideo() {
            throw new RuntimeException("Synchronized processing is not allowed here");
        }

        public void processVideoAsync() {
            builderParent.checkInputAndOutput();
            builderParent.flags.add(builderParent.outputPath);

            new Thread(this).start();
        }

        @Override
        public void run() {
            final String ffmpegArguments[] = builderParent.convertFlagsToArray();
            final int returnCode = process(ffmpegArguments);
            if (returnCode == 0) {
                builderParent.deleteInputFileIfNecessary();
                fireProcessingSucceededEvent();
            } else {
                fireProcessingFailedEvent(returnCode);
            }
        }

        private void fireProcessingSucceededEvent() {
            final ProcessingListener listener = processingListener.get();
            if (listener != null) {
                listener.onSuccess(new VideoProcessingResult(SUCCESS_CODE, builderParent.outputPath));
            }
        }

        private void fireProcessingFailedEvent(int returnCode) {
            final ProcessingListener listener = processingListener.get();
            if (listener != null) {
                listener.onFailure(new VideoProcessingResult(returnCode, builderParent.inputPath));
            }
        }
    }
}
