import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class NamePlugin extends JavaPlugin {
	
	private static final String API_URL = "http://urlapi/api/client"
	private static final String API_KEY = "API_KEY"
	private static final String PRODUCT_NAME = "NamePlugin"
	

	private boolean validateLicense() {
		final String licensekey = config.getString("license.key");
		
		try {
			URL url = new URL(API_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "aplication/json");
			connection.setRequestProperty("Authorization", API_KEY);
			connection.setDoOutput(true);

			String jsonInputString = String.format("{\"licensekey\": \"%s\", \"product\": \"%s\"}", licenseKey, PRODUCT_NAME);
			try (OutputStream outputStream = connection.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
				outputStream.write(input, 0, input.length);
			}
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))){
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
				return "SUCCESS".equals(jsonResponse.get("status_id").getAsString());
			}
		} catch (Exception e) {
			getLogger.severe("Error al validar la licencia " + e.getMessage());
			return false;
		}
	}
}