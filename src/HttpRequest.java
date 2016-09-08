import java.util.HashMap;
import java.util.Map;

/**
 * Created by razvan on 06.09.2016.
 */
public class HttpRequest {
    private enum Methods {
        GET,
        HEAD
    }

    private String rawHeader;
//    private boolean parsed = false;
    private Methods method;
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
//        parsed = true;
        // parse request line

        // parse header fields

        return StatusCode.OK;
    }

//    public boolean isParsed() {
//        return parsed;
//    }
}
