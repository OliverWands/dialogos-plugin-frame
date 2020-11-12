package dialogos.frame;

import org.json.JSONObject;

public interface Marshalling
{
    /**
     * The JSONObject will return the complete state of the instance of the implementing class.
     * @return The generated JSONObject
     */
    JSONObject marshal();

    boolean unmarshal(JSONObject jsonObject);

    JSONObject marshalStruct();

    boolean unmarshalStruct(JSONObject jsonObject);
}
