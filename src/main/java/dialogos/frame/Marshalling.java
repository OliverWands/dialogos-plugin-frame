package dialogos.frame;

import org.json.JSONObject;

public interface Marshalling
{
    /**
     * The JSONObject will return the structural state of the instance of the implementing class.
     * Specific values can be left out.
     *
     * @return The generated JSONObject
     */
    JSONObject marshal();

    /**
     * The calling instance will take over the state given in the JSON.
     *
     * @param jsonObject Contains the state.
     * @return Whether or not the unmarshal succeeded.
     */
    boolean unmarshal(JSONObject jsonObject);
}
