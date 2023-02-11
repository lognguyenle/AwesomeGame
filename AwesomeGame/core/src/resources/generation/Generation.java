package resources.generation;
import java.io.File;
import java.util.*;

public class Generation {
    private File data;
    private Map<String, String> cities;

    public Generation(String dataPath) {
        data = new File(dataPath);
    }
    
    // TODO: Figure out effective generation pattern 
    private void readGeoData() {
        // Step 0: Buffered reader to read line by line, get all tokens
        // Step 1: Determine if city is "ruin" or "functional"

        // Step 2: Determine based on population what city type will be
        // City type will allow for generation of factions/etc.

        // Step 3: generate nearby ruins? things? based off of monte carlo sampling between existing cities
    }
}
