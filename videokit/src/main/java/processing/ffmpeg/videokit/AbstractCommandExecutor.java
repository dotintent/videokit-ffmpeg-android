package processing.ffmpeg.videokit;

import java.io.File;
import java.util.List;

/**
 * Created by Ilja on 19.07.16.
 * Copyright by inFullMobile
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
abstract class AbstractCommandExecutor {
    private static final String FFMPEG_PROGRAM_NAME = "ffmpeg";

    private final String inputPath;
    private final List<String> flags;
    private final boolean deleteInput;

    protected String outputPath;

    AbstractCommandExecutor(String outputPath, String inputPath, boolean deleteInput,
                            List<String> flags) {
        this.outputPath = outputPath;
        this.inputPath = inputPath;
        this.deleteInput = deleteInput;
        this.flags = flags;
    }

    protected String[] flagsToArgs() {
        final String ffmpegArguments[] = new String[flags.size() + 1];
        for (int i = 0; i < flags.size(); i++) {
            ffmpegArguments[i + 1] = flags.get(i);
        }

        ffmpegArguments[0] = FFMPEG_PROGRAM_NAME;

        return ffmpegArguments;
    }

    protected void processResultCode(int ffmpegReturnCode) {
        if (ffmpegReturnCode != VideoKit.FFMPEG_SUCCESS_RETURN_CODE) {
            deleteOutputFile();
        } else {
            deleteInputIfNecessary();
        }
    }

    private void deleteOutputFile() {
        final File outputFile = new File(outputPath);
        if (outputFile.exists()) {
            outputFile.delete();
            outputPath = null;
        }
    }

    private void deleteInputIfNecessary() {
        final File inputFile = new File(inputPath);
        if (deleteInput && inputFile.exists()) {
            inputFile.delete();
        }
    }
}
