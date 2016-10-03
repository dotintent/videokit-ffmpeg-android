package processing.ffmpeg.videokit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by Ilja on 11.08.16.
 * Copyright by inFullMobile
 */
public class CommandBuilderTest {
    private String testPath;

    @Mock VideoKit videoKit;
    private ArgumentCaptor<String[]> argumentCaptor = ArgumentCaptor.forClass(String[].class);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(videoKit.process(argumentCaptor.capture())).thenReturn(0);
        testPath = getTestFilePath();
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnNullInputPath() {
        //given
        final CommandBuilder builder = new VideoCommandBuilder(null);

        //when
        builder.build();

    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnIncorrectInputPath() {
        //given
        final CommandBuilder builder = new VideoCommandBuilder(null);

        //when
        builder.addInputPath("aaaaa");
    }

    @Test
    public void shouldCreateCorrectBuilder() {
        //given
        final CommandBuilder builder = getCorrectCommandBuilder();

        //when
        builder.build();
    }

    @Test
    public void shouldAppendFewInputPaths() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().addInputPath(testPath);
        final String[] expectedFlags = { "ffmpeg", "-i", testPath, "-i", testPath, testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendOverwriteFlag() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().overwriteOutput();
        final String[] expectedFlags = { "ffmpeg", "-i", testPath, "-y", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendTrimFlags() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().trimForDuration(0, 30);
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-ss", "0", "-t", "30", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendNoAudioFlag() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().withoutAudio();
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-an", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendCopyVideoFlag() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().copyVideoCodec();
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-vcodec", "copy", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendCropFlags() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().addCrop(0, 0, 100, 100);
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-vf", "crop=100:100:0:0", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendCustomCommand() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().addCustomCommand("--KABOOOOM!!!");
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "--KABOOOOM!!!", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendLimitBitrateFlag() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().limitVideoBitrate("713K");
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-b:v", "713K", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendExperimentalFlagToTheEnd() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().experimentalFlag();
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-strict", "-2", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendLimitFramerateFlag() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().limitFrameRate(25);
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-framerate", "25", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    @Test
    public void shouldAppendFastTuneFlag() {
        // given
        final CommandBuilder builder = getCorrectCommandBuilder().setTuneToFast();
        final String[] expectedFlags =
                { "ffmpeg", "-i", testPath, "-tune", "fastdecode", "-tune", "zerolatency", testPath };

        // when
        builder.build().execute();

        // then
        assertTrue(areStringArraysEqual(argumentCaptor.getValue(), expectedFlags));
    }

    private CommandBuilder getCorrectCommandBuilder() {
        return new VideoCommandBuilder(videoKit)
                .addInputPath(testPath)
                .outputPath(testPath);
    }

    private String getTestFilePath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource("test");
        return resource.getPath();
    }

    private boolean areStringArraysEqual(String[] actual, String[] expected) {
        if (actual.length != expected.length) {
            return false;
        }

        for (int i = 0; i < actual.length; i++) {
            if (!actual[i].equals(expected[i])) {
                return false;
            }
        }

        return true;
    }
}
