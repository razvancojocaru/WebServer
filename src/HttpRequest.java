import java.util.HashMap;
import java.util.Map;

/**
 * Created by razvan on 06.09.2016.
 */
public class HttpRequest {

    private String rawHeader;
    private Methods method;
    private String uri;
    private String httpVersion;
    private Map<String, String> headers;

    public HttpRequest(String rawHeader) {
        this.rawHeader = rawHeader;
        this.headers = new HashMap<>();
    }

    /**
     * Reads a HTTP request header from a raw string.
     * Performs sintactic validation.
     */
    public synchronized StatusCode parse() {
        String[] headerLines = rawHeader.split("\r\n");

        // parse request line
        // <Method> <URI> <HTTP_version>
        String[] requestLine = headerLines[0].split(" ");
        try {
            method = Methods.valueOf(requestLine[0]);
        } catch (IllegalArgumentException e) {
            return StatusCode.BadRequest;
        }
        uri = requestLine[1];
        httpVersion = requestLine[2];
        if (!httpVersion.contains("HTTP/")) {
            return StatusCode.BadRequest;
        }
        httpVersion = httpVersion.split("HTTP/")[1];
        if (!httpVersion.equals("1.1") && !httpVersion.equals("1.0")) {
            return StatusCode.BadRequest;
        }

        // parse header fields
        for (int i = 1; i < headerLines.length; i++) {
            String[] headerLine = headerLines[i].split(": ");
            if (headerLine.length != 2) {
                return StatusCode.BadRequest;
            }
            headers.put(headerLine[0], headerLine[1]);
        }

        return StatusCode.OK;
    }

    public String getUri() {
        return uri;
    }

    public Methods getMethod() {
        return method;
    }

}
