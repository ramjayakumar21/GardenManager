package persistence;

import model.Garden;
import model.Plant;
import model.PlantBed;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// class for reading JSON file and retrieving data from it
public class ReaderJson {
    private String sourceDir;

    //EFFECTS: creates new ReaderJson object with given string as input
    public ReaderJson(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    //EFFECTS: reads source string from instance and produces Garden object
    //         throws IOException if there is an error while reading the files
    public Garden readSource() throws IOException {
        String jsonData = readString(sourceDir);
        return parseGarden(new JSONObject(jsonData));


    }

    //EFFECTS: reads file as string and returns it
    public String readString(String str) throws IOException {
        StringBuilder sourceBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(str), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sourceBuilder.append(s));
        }

        return sourceBuilder.toString();
    }

    //EFFECTS: converts given JSONObject into Garden object
    public Garden parseGarden(JSONObject jsonObj) {
        Garden g = new Garden(new ArrayList<>());
        addPlantBeds(g, jsonObj);
        return g;
    }

    //MODIFIES: g
    //EFFECTS: adds plant beds from jsonObj to g
    public void addPlantBeds(Garden g, JSONObject jsonObj) {
        JSONArray jsonArray = jsonObj.getJSONArray("plantBedArrayList");
        for (Object objJson : jsonArray) {
            JSONObject nextPlantBed = (JSONObject) objJson;
            addPlantBed(g,nextPlantBed);
        }
    }

    //MODIFIES: g
    //EFFECTS: adds given json object as new plant bed with plants to g
    public void addPlantBed(Garden g, JSONObject nextPlantBed) {
        String name = nextPlantBed.getString("name");
        PlantBed pb = new PlantBed(name);
        JSONArray jsonArray = nextPlantBed.getJSONArray("plantArrayList");
        for (Object objJson : jsonArray) {
            JSONObject nextPlant = (JSONObject) objJson;
            addPlant(pb, nextPlant);
        }
        g.addPlantBed(pb);
    }

    public void addPlant(PlantBed pb, JSONObject nextPlant) {
        String name = nextPlant.getString("name");
        String lifeStage = nextPlant.getString("lifeStage");
        String waterCycle = nextPlant.getString("waterCycle");
        String plantType = nextPlant.getString("plantType");
        Boolean isDry = nextPlant.getBoolean("isDry");
        Plant p = new Plant(name,waterCycle, lifeStage, plantType);
        p.setDry(isDry);
        pb.addPlant(p);
    }
}
