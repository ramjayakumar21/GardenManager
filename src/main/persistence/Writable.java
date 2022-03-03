package persistence;

import org.json.JSONObject;

//interface to ensure all classes have method to convert themselves into JSONObject
public interface Writable {

    // EFFECTS: returns this object as JSON object
    JSONObject toJson();
}
