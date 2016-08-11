package processing.ffmpeg.videokit;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Created by Ilja on 11.08.16.
 * Copyright by inFullMobile
 */
public class CommandBuilderTest {
    private String testPath;

    @Before
    public void setUp() {
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

    private String getTestFilePath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource("test");
        return resource.getPath();
    }

    @Test
    public void shouldCreateCommand() {
        //given
        final CommandBuilder builder = new VideoCommandBuilder(null);
        builder.addInputPath(testPath)
                .addOutputPath(testPath);
        final String[] expectedFlags = { "ffmpeg", "-i", testPath, testPath };

        //when
        final Command command = builder.build();

        //then
        final String[] actualFlags = getFlagsFromCommandWithReflection(command);
        assertEquals(expectedFlags.length, actualFlags.length);
        for (int i = 0; i < actualFlags.length; i++) {
            assertEquals(actualFlags[i], expectedFlags[i]);
        }
    }

    private String[] getFlagsFromCommandWithReflection(Command command) {
        try {
            final Method method = command.getClass().getDeclaredMethod("getArgumentsAsArray");
            method.setAccessible(true);
            return (String[]) method.invoke(command);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[0];
    }
}
