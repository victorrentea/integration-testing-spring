package victor.testing.spring.feed;
// DONE

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Disabled
@SpringBootTest
public class FeedProcessorWithMockTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @MockBean
   private IFileRepo fileRepoMock;

   @Test
   public void oneFileWithOneLine() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one line"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("two.txt"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() throws IOException {
      when(fileRepoMock.getFileNames()).thenReturn(Arrays.asList("one.txt", "two.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one line"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }
}
