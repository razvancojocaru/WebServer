# WebServer
Lightweight Java implementation of a HTTP 1.1 web server

Implemented HTTP Methods:
GET
HEAD

Any method not implemented will generate a 400 Bad Request error


Implemented HTTP Header Fields:
Content-Length

Any request containing a not implemented header will have those
specific headers ignored.