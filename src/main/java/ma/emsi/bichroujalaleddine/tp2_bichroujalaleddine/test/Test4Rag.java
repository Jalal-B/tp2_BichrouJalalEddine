package ma.emsi.bichroujalaleddine.tp2_bichroujalaleddine.test;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStore;

public class Test4Rag {

    public interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {
        String apiKey = System.getenv("GEMINIKEY");
        if (apiKey == null) {
            System.err.println("Variable d'environnement GEMINIKEY non définie !");
            return;
        }

        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gemini-2.5-flash")
                .temperature(0.3)
                .build();

        String nomDocument = "infos.txt";
        Document document = FileSystemDocumentLoader.loadDocument(nomDocument);
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();

        // Test 1
        String question1 = "Comment s'appelle le chat de Pierre ?";
        System.out.println("Q1 : " + question1);
        System.out.println("R1 : " + assistant.chat(question1));

        // Test 2
        String question2 = "Pierre appelle son chat. Qu'est-ce qu'il pourrait dire ?";
        System.out.println("Q2 : " + question2);
        System.out.println("R2 : " + assistant.chat(question2));

        // Test 3 (modifie infos.txt AVANT LA RUN : "La capitale de la France est Marseille.")
        String question3 = "Quelle est la capitale de la France ?";
        System.out.println("Q3 : " + question3);
        System.out.println("R3 : " + assistant.chat(question3));
    }
}

