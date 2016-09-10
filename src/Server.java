import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles socket connections and multitasking.
 * Uses byte streams for IO.
 * Uses Executors for concurrency.
 */
public class Server {
    private int port = 8080;
    private String dir = "/home/razvan/webfiles";
    private Path rootDir;
    private ExecutorService threadPool;
    private ServerSocket serverSocket;

    public Server() {
        threadPool = Executors.newFixedThreadPool(100);
    }

    public void start() {
        try {
            rootDir = Paths.get(dir);
            if (!Files.exists(rootDir)) {
                throw new InvalidPathException(" ", " ");
            }
        } catch (InvalidPathException e) {
            System.err.println("Server root directory not found");
            e.printStackTrace();
            return;
        }

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new HandleRequest(clientSocket, rootDir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


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

            // read request header from socket
            StringBuffer sb = new StringBuffer();
            String header;
            header = fromSocket.readLine();
            while ((header != null) &&
                    (header.length() != 0)) {
                sb.append(header + "\r\n");
                header = fromSocket.readLine();
            }
            System.out.println(sb.toString());

            // parse header
            HttpRequest request = new HttpRequest(sb.toString());
            StatusCode status = request.parse();

            // build response message
            HttpResponse response = new HttpResponse(status, request, dir);
            InputStream fileStream = response.build();

            toSocket.write(response.toString());
            toSocket.flush();

            // if necessary, send file to client
            if (fileStream != null) {
                BufferedReader fromFile = new BufferedReader(
                        new InputStreamReader(fileStream));
                String fileLine;
                try {
                    while (((fileLine = fromFile.readLine()) != null) &&
                            (fileLine.length() != 0)) {
                        toSocket.write(fileLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                toSocket.flush();
            }
            System.out.println(response.getStatusLine());
            System.out.println("DONE");

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
