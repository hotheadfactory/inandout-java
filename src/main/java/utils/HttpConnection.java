package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class HttpConnection {
    private final String USER_AGENT = "Mozilla/5.0";

    public Map<String, String> sendGet(String targetUrl) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", USER_AGENT);
        return setRequest(con);
    }

    private Map<String, String> setRequest(HttpURLConnection con) throws IOException {
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        System.out.println("HTTP 응답 코드 : " + responseCode);
        Map<String, String> responseMap = toHashMap(responseCode, response);
        if (responseMap != null) return responseMap;
        throw new InputMismatchException("연결 실패");
    }

    public Map<String, String> sendPost(String targetUrl, String parameters) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST"); // HTTP POST 메소드 설정
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();
        return setRequest(con);
    }

    private Map<String, String> toHashMap(int responseCode, StringBuffer response) {
        Map<String, String> responseMap = new HashMap<>();
        if(responseCode == 200) {
            System.out.println(response);
            String[] responseList = response.toString().replace("{", "").replace("}", "").split(",");
            for(String string : responseList) {
                String[] eachArgument = string.replace("\"", "").replace("[", "").replace("]", "").split(":");
                responseMap.put(eachArgument[0], eachArgument[1]);
            }
            return responseMap;
        }
        return null;
    }
}
