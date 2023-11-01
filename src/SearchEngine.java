import java.io.IOException;

public interface SearchEngine {
    void search(String query);
    void remove(String documentName);

    void load(String filePath) throws IOException;
    void clearList();
    PerformanceMetrics getPerformanceMetrics();

}
