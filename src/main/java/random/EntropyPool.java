package random;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class EntropyPool {

    private LinkedList<Integer> dataPool;

    public EntropyPool(JSONObject data) {
        parseJSONObject(data);
    }

    private void parseJSONObject(JSONObject data) {
        if (data == null)
            System.exit(1);

        dataPool = new LinkedList<Integer>();

        JSONArray values = data.getJSONArray("data");
        for (int i = 0; i < values.length(); i++) {
            dataPool.add(values.getInt(i));
        }
    }

    public Integer getNext() {
        Integer value = dataPool.poll();
        return value;
    }

}
