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
