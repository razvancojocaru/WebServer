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

    private final static int BUFSIZE = 1024;
    private Socket clientSocket;
    private String dir;

    public HandleRequest(Socket socket, String dir) {
        this.clientSocket = socket;
        this.dir = dir;
    }

    @Override
    public void run() {
        try {
//            byte[] buffer = new byte[BUFSIZE];
//            int count;
//            InputStream fromSocket = clientSocket.getInputStream();
//            OutputStream toSocket = clientSocket.getOutputStream();
//
//            // read file name from socket
//            count = fromSocket.read(buffer);
//            String fileName = new String(buffer).substring(0, count);
//            System.out.println(fileName);
//            // send ACK
//            toSocket.write("ok".getBytes());
//            toSocket.flush();
//
//            System.out.println("DONE");

            BufferedReader fromSocket = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter toSocket = new BufferedWriter(
                    new OutputStreamWriter(clientSocket.getOutputStream()));
            String header;
            header = fromSocket.readLine();
            while ((header != null) &&
                    (header.length() != 0)) {
                System.out.println(header);
                header = fromSocket.readLine();
            }

            System.out.println("DONE");

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
