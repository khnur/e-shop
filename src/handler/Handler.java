package handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Handler {
    static Map<String, String> parseJsonRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();

        String str;
        while ((str = bufferedReader.readLine()) != null)
            stringBuilder.append(str);

        String body = stringBuilder.toString();
        body = body.substring(1, body.length() - 1);
        body = body.replaceAll("\"", "");

        Map<String, String> bodyMap = new HashMap<>();
        Arrays.stream(body.split(","))
                .forEach(string -> bodyMap.put(string.split(":")[0].trim(), string.split(":")[1].trim()));
        return bodyMap;
    }

    static void streamWrite(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getRequestHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
