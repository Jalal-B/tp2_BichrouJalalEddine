package ma.emsi.bichroujalaleddine.tp2_bichroujalaleddine.test;

import ma.emsi.bichroujalaleddine.tp2_bichroujalaleddine.tools.MeteoTool;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.chat.ChatModel;

public class Test6 {

    public static void main(String[] args) {
        String apiKey = System.getenv("GEMINIKEY");
        if (apiKey == null) {
            System.err.println("Variable d'environnement GEMINIKEY non définie !");
            return;
        }

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .temperature(0.2)
                .logRequestsAndResponses(true) // important pour les logs bonus
                .modelName("gemini-2.5-flash")
                .build();

        AssistantMeteo assistant = AiServices.builder(AssistantMeteo.class)
                .chatModel(model)
                .tools(new MeteoTool())
                .build();

        // Test météo de villes existantes
        System.out.println("Paris : " + assistant.chat("Quelle est la météo aujourd'hui à Paris ?"));
        System.out.println("Marrakech : " + assistant.chat("J'ai prévu d'aller aujourd'hui à Marrakech, dois-je prendre un parapluie ?"));
        System.out.println("Tokyo : " + assistant.chat("Quel temps fait-il à Tokyo ?"));

        // Test avec ville inexistante
        System.out.println("Ville inventée : " + assistant.chat("Quelle est la météo à CalimeroCity ?"));

        // Test avec question hors contexte (pas météo)
        System.out.println("Math : " + assistant.chat("Combien font 6 x 13 ?"));
    }
}
