package victor.testing.spring.feed;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Primary
public class FileRepoFake implements IFileRepo {
   private final Map<String, List<String>> files = new HashMap<>();

   @Override
   public Collection<String> getFileNames() {
      return files.keySet();
   }

   @Override
   public Stream<String> openFile(String fileName) {
      return files.get(fileName).stream();
   }

   public void addFile(String fileName, List<String> lines) {
      files.put(fileName, lines);
   }

   public void clearFiles() {
      files.clear();
   }
}
