import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Processes a valid HTTP Request.
 */
public class HttpResponse {
    private StatusCode status;
    private HttpRequest request;
    private String statusLine;
    private Map<String, String> headers;
    private Path rootDir;
    private InputStream resourceStream;
    private Path uriFullPath;

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
        // also perform security check for ".."
        String uri = request.getUri();
        if ((uri.equals("/")) ||
                (uri.contains(".."))){
            uri = "index.html";
        }
        try {
            uriFullPath = Paths.get(rootDir.toString(), uri);
            if (!Files.exists(uriFullPath)) {
                throw new InvalidPathException(" ", " ");
            }
        } catch (InvalidPathException e) {
            statusLine = StatusCode.NotFound.getStatusLine();
            return null;
        }
        statusLine = status.getStatusLine();

        try {
            resourceStream = new FileInputStream(uriFullPath.toString());
        } catch (FileNotFoundException e) {
            statusLine = StatusCode.NotFound.getStatusLine();
            return null;
        }

        handleHttpHeaders();

        handleHttpMethod();

        return resourceStream;
    }

    private void handleHttpMethod() {
        if (request.getMethod() == Methods.GET) {

        } else if (request.getMethod() == Methods.HEAD) {
            resourceStream = null;
        }
    }

    private void handleHttpHeaders() {
        // generate Content-Length
        long fileSize = uriFullPath.toFile().length();
        headers.put("Content-Length", Objects.toString(fileSize, null));
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
        return sb.toString();
    }
}
