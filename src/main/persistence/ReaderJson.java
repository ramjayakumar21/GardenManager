package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// represents a reader that reads a Garden object from JSON data in file
//      uses methods from JsonSerializationDemo.JsonReader:
//      https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class ReaderJson {
    private String sourcePath;

    //EFFECTS: creates new ReaderJson object with given string as input
    public ReaderJson(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    //EFFECTS: reads source string from instance and produces Garden object
    //         throws IOException if there is an error while reading the files
    public Garden readSource() throws IOException {
        String jsonData = readString(sourcePath);
        return parseGarden(new JSONObject(jsonData));
    }

    //EFFECTS: reads file as string and returns it
    //copied from JsonSerializationDemo
    public String readString(String str) throws IOException {
        StringBuilder sourceBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(str), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sourceBuilder.append(s));
        }

        return sourceBuilder.toString();
    }

    //MODIFIES: g
    //EFFECTS: converts given JSONObject into Garden object
    public Garden parseGarden(JSONObject jsonObj) {
        Garden g = new Garden(new ArrayList<>());
        addGardenFields(g, jsonObj);
        return g;
    }

    //MODIFIES: g
    //EFFECTS: parses plant beds and plants from given jsonObj and adds to given garden
    public void addGardenFields(Garden g, JSONObject jsonObj) {
        JSONArray jsonArray = jsonObj.getJSONArray("plantBedArrayList");
        for (Object objJson : jsonArray) {
            JSONObject nextPlantBed = (JSONObject) objJson;
            addPlantBed(g,nextPlantBed);
        }
    }

    //MODIFIES: g, pb
    //EFFECTS: parses plant bed from nextPlantBed and adds it to given garden
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

    //MODIFIES: pb
    //EFFECTS: parses plant from nextPlant and adds it to given plant bed
    public void addPlant(PlantBed pb, JSONObject nextPlant) {
        String name = nextPlant.getString("name");
        String lifeStage = nextPlant.getString("lifeStage");
        String waterCycle = nextPlant.getString("waterCycle");
        String plantType = nextPlant.getString("plantType");
        Boolean isDry = nextPlant.getBoolean("isDry");
        Plant p = new Plant(name,waterCycle,plantType,lifeStage);
        p.setDry(isDry);
        pb.addPlant(p);
    }
}
