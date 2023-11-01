package server;
import java.io.*;
import java.net.*;
import org.json.*;

public class Whisper {
    private static final String API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN = "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";
    private static final String MODEL = "whisper-1";

    public Whisper() {

    }
    
    // Helper method to write a parameter to the output stream in multipart form data format
    private static void writeParameterToOutputStream(
        OutputStream outputStream,
        String parameterName,
        String parameterValue,
        String boundary
        ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        (
            "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
        ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    private static void writeFileToOutputStream(
        OutputStream outputStream,
        File file,
        String boundary
        ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
        (
        "Content-Disposition: form-data; name=\"file\"; filename=\"" +
        file.getName() +
        "\"\r\n"
        ).getBytes()
            );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());


        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    private static String handleSuccessResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();


        JSONObject responseJson = new JSONObject(response.toString());


        String generatedText = responseJson.getString("text");

        return generatedText;
    }

    private static String handleErrorResponse(HttpURLConnection connection)
        throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
            new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();

        return errorResult;
    }

    public String askWhisper(File file) throws IOException, URISyntaxException {        
        // Set up HTTP connection
        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        
        // Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);
        
        // Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();
        
        // Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);
        
        // Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);
        
        // Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
        
        // Flush and close output stream
        outputStream.flush();
        outputStream.close();
        
        // Get response code
        int responseCode = connection.getResponseCode();
        String responseMessage;
        
        // Check response code and handle response accordingly
        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseMessage = handleSuccessResponse(connection);
        } else {
            responseMessage = handleErrorResponse(connection);
        }
        
        // Disconnect connection
        connection.disconnect();

        return responseMessage;
    }
}