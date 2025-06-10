package server;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

dpublic class Response {
    private final OutputStream output;
    private final HashMap<String, String> headers = new HashMap<>();
    private int status = 200;
    private String body = "";

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setStatus(int code) {
        this.status = code;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body == null ? "" : body;
    }

    public void send() {
        try {
            if (!headers.containsKey("Content-Type")) {
                headers.put("Content-Type", "application/json");
            }

            byte[] bodyBytes = body.getBytes("UTF-8");
            headers.put("Content-Length", String.valueOf(bodyBytes.length));

            PrintWriter writer = new PrintWriter(output, false, java.nio.charset.StandardCharsets.UTF_8);
            writer.print("HTTP/1.1 " + status + " \r\n");

            for (String key : headers.keySet()) {
                writer.print(key + ": " + headers.get(key) + "\r\n");
            }

            writer.print("\r\n");
            writer.flush();

            output.write(bodyBytes);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
