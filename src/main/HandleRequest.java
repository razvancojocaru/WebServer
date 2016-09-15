package ro.razvancojocaru.main;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

/**
 * Process a HTTP request and send response.
 * Uses character streams for headers and byte streams for sending resources.
 */
class HandleRequest implements Runnable {
    private Socket clientSocket;
    private Path dir;

    public HandleRequest(Socket socket, Path dir) {
        this.clientSocket = socket;
        this.dir = dir;
    }

    @Override
    public void run() {
        try {

            BufferedReader fromSocket = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter toSocket = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));
            OutputStream socketOutput = clientSocket.getOutputStream();

            // read request header from socket
            StringBuffer sb = new StringBuffer();
            String header;
            header = fromSocket.readLine();
            while ((header != null) &&
                    (header.length() != 0)) {
                sb.append(header + "\r\n");
                header = fromSocket.readLine();
            }

            // parse header
            HttpRequest request = new HttpRequest(sb.toString());
            StatusCode status = request.parse();

            // build response message
            HttpResponse response = new HttpResponse(status, request, dir);
            InputStream fileStream = response.build();

            toSocket.write(response.toString());
            toSocket.write("\r\n");
            toSocket.flush();

            // if necessary, send file to client
            if (fileStream != null) {
                byte[] buffer = new byte[1024];
                int size;
                try {
                    while ((size = fileStream.read(buffer)) != -1) {
                        socketOutput.write(buffer, 0, size);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                toSocket.flush();
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
