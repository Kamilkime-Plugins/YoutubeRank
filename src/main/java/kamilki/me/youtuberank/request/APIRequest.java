package kamilki.me.youtuberank.request;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public enum APIRequest {

    VIDEO("videos?part=snippet&id=%s&key=%s"),
    CHANNEL("channels?part=statistics&id=%s&key=%s");

    private static final String apiURL = "https://www.googleapis.com/youtube/v3/";
    private static final JSONParser jsonParser = new JSONParser();

    private final String endpoint;

    APIRequest(final String endpoint) {
        this.endpoint = endpoint;
    }

    public JSONObject makeRequest(final String id, final String apiKey) {
        try {
            final URL url = new URL(apiURL + String.format(this.endpoint, id, apiKey));
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            final BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final StringBuilder stringBuilder = new StringBuilder();

            String contentLine = "";
            while ((contentLine = input.readLine()) != null) {
                stringBuilder.append(contentLine);
            }

            input.close();
            connection.disconnect();

            return (JSONObject) jsonParser.parse(stringBuilder.toString());
        } catch (final Exception exception) {
            return null;
        }
    }

}
