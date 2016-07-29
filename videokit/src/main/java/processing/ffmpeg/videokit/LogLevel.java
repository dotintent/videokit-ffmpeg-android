package processing.ffmpeg.videokit;

/**
 * Created by Ilja Kosynkin on 12.07.2016.
 * Copyright by inFullMobile
 */
@SuppressWarnings("unused")
public enum LogLevel {
    NO_LOG(0), ERRORS_ONLY(1), FULL(2);

    private final int integerValue;

    LogLevel(int value) {
        integerValue = value;
    }

    public int getValue() {
        return integerValue;
    }
}
