package victor.testing.spring.feed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.testing.spring.tools.MeasureTotalTestTimeListener.MeasureRealTime;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@MeasureRealTime
public class FeedProcessorWithMockTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @MockBean
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
      when(fileRepoMock.getFileNames()).thenReturn(List.of("one.txt","two.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("one"));
      when(fileRepoMock.openFile("two.txt")).thenReturn(Stream.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }

   @Test
   public void doesNotCountHashedLines() {
      when(fileRepoMock.getFileNames()).thenReturn(List.of("one.txt"));
      when(fileRepoMock.openFile("one.txt")).thenReturn(Stream.of("#one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(0);
   }

   // TODO IMAGINE EXTRA DEPENDENCY
}
