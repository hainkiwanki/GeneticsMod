package com.hainkiwanki.geneticsmod.cap.research;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResearchNodeLoader {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Map<String, ResearchNode> researchNodeCache = new HashMap<>();

    public static void loadResearchNodes() {
        try (Reader reader = new InputStreamReader(ResearchNodeLoader.class.getResourceAsStream("/assets/geneticsmod/research_nodes.json"))) {

            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray nodesArray = json.getAsJsonArray("nodes");

            for (JsonElement element : nodesArray) {
                JsonObject nodeObject = element.getAsJsonObject();
                String id = nodeObject.get("id").getAsString();
                String name = nodeObject.get("name").getAsString();
                String description = nodeObject.get("description").getAsString();
                List<String> prerequisites = new ArrayList<>();

                for (JsonElement prereq : nodeObject.getAsJsonArray("prerequisites")) {
                    prerequisites.add(prereq.getAsString());
                }

                ResearchNode node = new ResearchNode(id, name, description, prerequisites);
                researchNodeCache.put(id, node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
