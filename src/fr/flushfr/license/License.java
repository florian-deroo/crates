package fr.flushfr.license;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import static fr.flushfr.crates.Main.getMainInstance;

public class License {

    public static DataLicense getLicenseResponse (String license) {
        try {
            URL url = new URL("https://flush-license.herokuapp.com/api/manager.php?type=read&license="+license+"&version="+getMainInstance().getDescription().getVersion()+"&plugin=crate&ip="+ InetAddress.getLocalHost().getHostAddress());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), DataLicense.class);
            }
        } catch (IOException e) {
            return null;
        }
    }
}
