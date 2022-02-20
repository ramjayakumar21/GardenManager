package persistence;

import model.Garden;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriterJson {
    private static final int TAB = 4;
    private PrintWriter printWriter;
    private String destinationPath;

    //EFFECTS: creates writer object and add gives path for destination file
    public WriterJson(String destinationPath) {
        this.destinationPath = destinationPath;
    }

    //MODIFIES: this
    //EFFECTS: opens writer for saving Garden to file
    //          throws FileNotFoundException if destination file can't be opened
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destinationPath));
    }

    //MODIFIES: this
    //EFFECTS: writes given Garden object to file
    public void writeToJson(Garden g) {
        JSONObject jsonObject = g.toJson();
        saveToFile(jsonObject.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        printWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        printWriter.print(json);
    }
}
