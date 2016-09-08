/**
 * Created by razvan on 08.09.2016.
 */
class Header {

}

enum StatusCode {
    OK(200, "OK"),
    BadRequest(400, "Bad Request"),
    NotFound(404, "Not Found"),
    InternalServerError(500, "Internal Server Error");

    StatusCode(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    private int code;
    private String reasonPhrase;
}
