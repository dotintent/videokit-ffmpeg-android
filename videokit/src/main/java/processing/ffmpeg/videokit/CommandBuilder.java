package processing.ffmpeg.videokit;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
class CommandBuilder implements ICommandBuilder {
    private static final String OVERWRITE_FLAG = "-y";
    private static final String INPUT_FILE_FLAG = "-i";
    private static final String TRIM_FLAG = "-ss";
    private static final String DURATION_FLAG = "-t";
    private static final String COPY_FLAG = "copy";
    private static final String VIDEO_CODEC_FLAG = "-vcodec";
    private static final String VIDEO_FILTER_FLAG = "-vf";
    private static final String VIDEO_BITRATE_FLAG = "-b:v";
    private static final String STRICT_FLAG = "-strict";
    private static final String EXPERIMENTAL_FLAG = "-2";
    private static final String THREADS_FLAG = "-threads";
    private static final String THREADS_NUMBER = "5";
    private static final String REMOVE_AUDIO_STREAM_FLAG = "-an";
    private static final String FRAME_RATE_FLAG = "-framerate";
    private static final String TUNE_FLAG = "-tune";
    private static final String FAST_DECODE = "fastdecode";
    private static final String ZERO_LATENCY = "zerolatency";

    private final List<String> flags = new ArrayList<>();

    private final VideoKit videoKit;

    private String outputPath;
    private boolean inputWasSet;

    CommandBuilder(VideoKit videoKit) {
        this.videoKit = videoKit;
    }

    @Override
    public CommandBuilder overwriteOutput() {
        flags.add(OVERWRITE_FLAG);
        return this;
    }

    @Override
    public CommandBuilder addInputPath(String inputFilePath) {
        final File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new RuntimeException("File provided by you does not exists");
        }

        inputWasSet = true;
        flags.add(INPUT_FILE_FLAG);
        flags.add(inputFilePath);
        return this;
    }

    @Override
    public CommandBuilder addOutputPath(String outputPath) {
        if (TextUtils.isEmpty(outputPath)) {
            throw new RuntimeException("It's not a good idea to pass empty path here");
        }

        this.outputPath = outputPath;
        return this;
    }

    @Override
    public CommandBuilder trimForDuration(int startPosition, int duration) {
        flags.add(TRIM_FLAG);
        flags.add(String.valueOf(startPosition));
        flags.add(DURATION_FLAG);
        flags.add(String.valueOf(duration));
        return this;
    }

    @Override
    public CommandBuilder withoutAudio() {
        flags.add(REMOVE_AUDIO_STREAM_FLAG);
        return this;
    }

    @Override
    public CommandBuilder copyVideoCodec() {
        flags.add(VIDEO_CODEC_FLAG);
        flags.add(COPY_FLAG);
        return this;
    }

    @Override
    public CommandBuilder addCrop(int x, int y, int width, int height) {
        flags.add(VIDEO_FILTER_FLAG);
        flags.add("crop=" + width + ":" + height + ":" + x + ":" + y);
        return this;
    }

    @Override
    public CommandBuilder addCustomCommand(String customCommand) {
        if (TextUtils.isEmpty(customCommand)) {
            return this;
        }

        final String[] splitedCommand = customCommand.trim().split(" ");
        Collections.addAll(flags, splitedCommand);
        return this;
    }

    @Override
    public CommandBuilder limitVideoBitrate(String bitrate) {
        if (TextUtils.isEmpty(bitrate)) {
            throw new RuntimeException("It's not a good idea to pass empty path here");
        }

        flags.add(VIDEO_BITRATE_FLAG);
        flags.add(bitrate);
        return this;
    }

    @Override
    public CommandBuilder addExperimentalFlag() {
        flags.add(STRICT_FLAG);
        flags.add(EXPERIMENTAL_FLAG);
        return this;
    }

    @Override
    public CommandBuilder setAutoThreadingFlag() {
        flags.add(THREADS_FLAG);
        flags.add(THREADS_NUMBER);
        return this;
    }

    @Override
    public CommandBuilder limitFrameRate(int framerate) {
        flags.add(FRAME_RATE_FLAG);
        flags.add(String.valueOf(framerate));
        return this;
    }

    @Override
    public CommandBuilder setTuneToFast() {
        flags.add(TUNE_FLAG);
        flags.add(FAST_DECODE);
        flags.add(TUNE_FLAG);
        flags.add(ZERO_LATENCY);
        return this;
    }

    @Override
    public Command build() {
        if (!inputWasSet) {
            throw new RuntimeException("You must specify input path");
        }

        if (TextUtils.isEmpty(outputPath)) {
            throw new RuntimeException("You must specify output path");
        }

        flags.add(outputPath);

        return new Command(flags, outputPath);
    }

    @Override
    public SyncCommandExecutor buildAndPassToSyncExecutor() {
        return videoKit.createSyncExecutorWithCommand(build());
    }

    @Override
    public AsyncCommandExecutor buildAndPassToAsyncExecutor(ProcessingListener listener) {
        return videoKit.createAsyncExecutorWithCommand(build(), listener);
    }
}
