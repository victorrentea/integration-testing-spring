package victor.testing.spring.feed;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Primary
@Component
//@Profile("dummy-file-repo")
public class FileRepoDummy implements IFileRepo {
   private final Map<String, String> fileContents = new HashMap<>();

   public void addFileContents(String fileName, String contents) {
      this.fileContents.put(fileName, contents);
   }

   @Override
   public Collection<String> getFileNames() {
      return fileContents.keySet();
   }

   @Override
   public Stream<String> openFile(String fileName) {
      return fileContents.get(fileName).lines();
   }
}
