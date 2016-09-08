import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles socket connections and multitasking.
 * Uses byte streams for IO.
 * Uses Executors for concurrency.
 */
public class Server {
    private int port = 8080;
    private String dir;
    private ExecutorService threadPool;
    private ServerSocket serverSocket;

    public Server() {
        threadPool = Executors.newFixedThreadPool(100);
    }

    public void start() {
        //TODO check directory availability

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new HandleRequest(clientSocket, dir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class HandleRequest implements Runnable {
    private Socket clientSocket;
    private String dir;

    public HandleRequest(Socket socket, String dir) {
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
            System.out.println("DONE");

            // parse header
            HttpRequest request = new HttpRequest(sb.toString());
            request.parse();

            toSocket.write("OK");
            toSocket.flush();

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
