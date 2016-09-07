/**
 * Main entry point.
 * Imports configuration parameters from config file and starts server.
 */
public class Main {
    public static void main(String[] args) {
        //TODO parse config file

        Server server = new Server();
        server.start();
    }
}
