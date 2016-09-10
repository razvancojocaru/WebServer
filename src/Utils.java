/**
 * Created by razvan on 08.09.2016.
 */

enum Methods {
    GET,
    HEAD
}

enum StatusCode {
    OK("200", "OK"),
    BadRequest("400", "Bad Request"),
    NotFound("404", "Not Found"),
    InternalServerError("500", "Internal Server Error"),
    NotImplemented("501", "Not Implemented");

    StatusCode(String code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    private String code;
    private String reasonPhrase;

    public String getCode() {
        return this.code;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public String getStatusLine() {
        return "HTTP/1.1 " + this.code + " " + this.reasonPhrase + "\r\n";
    }
}
