package ma.emsi.bichroujalaleddine.tp2_bichroujalaleddine.tools;

import dev.langchain4j.agent.tool.Tool;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URI;
import java.util.Scanner;

public class MeteoTool {
    @Tool("Donne la météo d'une ville")
    public String donneMeteo(String ville) {
        try {
            String apiUri = "https://wttr.in/" + ville + "?format=3";
            HttpURLConnection connection = (HttpURLConnection) new URI(apiUri).toURL().openConnection();
            connection.setRequestMethod("GET");
            Scanner scanner = new Scanner(connection.getInputStream());
            String response = scanner.useDelimiter("\\A").next();
            scanner.close();
            return "Météo actuelle à " + ville + " : " + response;
        } catch (IOException e) {
            return "Erreur lors de la récupération de la météo pour " + ville + " : " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}