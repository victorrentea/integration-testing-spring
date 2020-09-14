package victor.testing.spring.feed;

import java.io.Reader;
import java.util.Collection;
import java.util.stream.Stream;

public interface IFileRepo {
   Collection<String> getFileNames();

   Stream<String> openFile(String fileName);
}
