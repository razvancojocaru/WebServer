import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Processes a valid HTTP Request.
 */
public class HttpResponse {
    private StatusCode status;
    private HttpRequest request;
    private String statusLine;
    private String content = null;
    private Map<String, String> headers;
    private Path rootDir;

    public HttpResponse(StatusCode status, HttpRequest request, Path dir) {
        this.status = status;
        this.request = request;
        this.rootDir = dir;
        this.headers = new HashMap<>();
    }

    public InputStream build() {
        if (status != StatusCode.OK) {
            statusLine = status.getStatusLine();
            return null;
        }

        // check if URI is valid
        Path fullPath;
        String uri = request.getUri();
        if (uri.equals("/")) {
            uri = "index.html";
        }
        try {
            fullPath = Paths.get(rootDir.toString(), uri);
            if (!Files.exists(fullPath)) {
                throw new InvalidPathException(" ", " ");
            }
        } catch (InvalidPathException e) {
            statusLine = StatusCode.NotFound.getStatusLine();
            return null;
        }
        System.out.println(fullPath.toString());


        System.out.println(Files.exists(fullPath));
        System.out.println(fullPath.toString());
        statusLine = status.getStatusLine();

        InputStream i;
        try {
            i = new FileInputStream(fullPath.toString());
        } catch (FileNotFoundException e) {
            statusLine = StatusCode.NotFound.getStatusLine();
            return null;
        }
        return i;
    }

    public String getStatusLine() {
        return statusLine;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(statusLine);
        for (Map.Entry header : headers.entrySet()) {
            sb.append(header.getKey() + ": " + header.getValue());
        }
        sb.append("\r\n");
        if (content != null) {
            sb.append(content + "\r\n");
        }
        return sb.toString();
    }
}
