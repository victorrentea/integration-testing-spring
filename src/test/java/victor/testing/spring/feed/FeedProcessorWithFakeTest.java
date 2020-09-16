package victor.testing.spring.feed;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import victor.testing.spring.tools.MeasureTotalTestTimeListener.MeasureRealTime;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@MeasureRealTime
public class FeedProcessorWithFakeTest {

   @Autowired
   private FeedProcessor feedProcessor;
   @Autowired
   private FileRepoFake fileRepoFake;

   @BeforeEach
   public void cleanup() {
      fileRepoFake.clearFiles();
   }

   @Test
   public void oneFileWithOneLine() {
      fileRepoFake.addFile("one.txt", List.of("one.txt"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(1);
   }

   @Test
   public void oneFileWith2Lines() {
      fileRepoFake.addFile("two.txt", List.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(2);
   }

   @Test
   public void twoFilesWith3Lines() {
      fileRepoFake.addFile("one.txt", List.of("one"));
      fileRepoFake.addFile("two.txt", List.of("one","two"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(3);
   }

   @Test
   public void doesNotCountHashedLines() {
      fileRepoFake.addFile("one.txt", List.of("#one"));
      assertThat(feedProcessor.countPendingLines()).isEqualTo(0);
   }
}
