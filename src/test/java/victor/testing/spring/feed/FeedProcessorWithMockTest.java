package victor.testing.spring.feed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedProcessorWithMockTest {

   @InjectMocks
   private FeedProcessor feedProcessor;
   @Mock
   private FileRepo fileRepoMock;

   @Test
   public void oneFileWithOneLine() {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("two.txt"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
   }

   // TODO IMAGINE EXTRA DEPENDENCY
}
