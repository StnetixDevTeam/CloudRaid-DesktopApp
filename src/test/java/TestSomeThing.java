import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Anton on 05.08.2016.
 */
public class TestSomeThing {
    public static void main(String[] args) {
        Path tmp = Paths.get("C:/tmp");
        Path newFolder = Paths.get("abc/foo");
        System.out.println(tmp.resolve(newFolder));
        if (!Files.exists(tmp.resolve(newFolder))){
            System.out.println("Create");

            try {
                Files.createDirectory(tmp.resolve(newFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
