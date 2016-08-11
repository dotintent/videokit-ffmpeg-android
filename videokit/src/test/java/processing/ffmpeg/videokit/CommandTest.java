package processing.ffmpeg.videokit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by Ilja on 11.08.16.
 * Copyright by inFullMobile
 */
public class CommandTest {
    VideoKit videoKit;

    private String testPath;

    @Before
    public void setUp() {
        testPath = getTestFilePath();

        videoKit = spy(new VideoKit());
        doReturn(0).when(videoKit).process(Mockito.any(String[].class));
    }

    private String getTestFilePath() {
        final ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource("test");
        return resource.getPath();
    }

    @Test
    public void shouldReturnSuccess() {
        //given
        final CommandBuilder builder = new VideoCommandBuilder(videoKit);
        final  Command command = builder.addInputPath(testPath)
                                        .addOutputPath(testPath)
                                        .build();

        //when
        final int result = command.execute().getCode();

        //then
        assertEquals(0, result);
    }
}
