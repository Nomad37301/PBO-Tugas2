package app;

import server.Server;

public class Main {
    public static final String API_KEY = "SECRET123";
    public static void main(String[] args) {
        Server server = new Server();
        server.start(8080);
    }
}
