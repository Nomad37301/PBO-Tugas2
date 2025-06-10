package server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Request {
    private String method;
    private String path;
    private String body = "";
    private final HashMap<String, String> headers = new HashMap<>();

    public Request(InputStream input) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        String line = reader.readLine();

        if (line == null || line.isEmpty()) throw new Exception("Invalid HTTP request");

        String[] requestLine = line.split(" ");
        if (requestLine.length < 2) throw new Exception("Malformed request line");
        method = requestLine[0];
        path = requestLine[1];

        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] header = line.split(":", 2);
            if (header.length == 2) {
                headers.put(header[0].trim().toLowerCase(), header[1].trim());
            }
        }

        int contentLength = 0;
        if (headers.containsKey("content-length")) {
            try {
                contentLength = Integer.parseInt(headers.get("content-length"));
            } catch (NumberFormatException ignored) {}
        }

        if (contentLength > 0) {
            char[] buffer = new char[contentLength];
            int read = reader.read(buffer, 0, contentLength);
            body = new String(buffer, 0, read);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body == null ? "" : body;
    }

    public String getHeader(String key) {
        return headers.getOrDefault(key.toLowerCase(), null);
    }
}
