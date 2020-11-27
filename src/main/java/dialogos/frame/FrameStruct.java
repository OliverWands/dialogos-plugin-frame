package dialogos.frame;

import dialogos.frame.utils.tags.TagIO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class FrameStruct implements Marshalling
{
    private String ID;
    private List<SlotStruct> slotList = new ArrayList<>();

    private File globalTags = null;
    private HashMap<String, String> globalGrammarTags = null;

    private File tags = null;
    private HashMap<String, String> grammarTags = new HashMap<>();

    private final Comparator<SlotStruct> slotComparator =
            (o1, o2) -> Boolean.compare(o1.isNotFilled(), o2.isNotFilled());

    public FrameStruct()
    {
    }

    public FrameStruct(String ID)
    {
        this.ID = ID;
    }

    public FrameStruct(String ID, List<SlotStruct> slotList)
    {
        this.ID = ID;
        this.slotList = slotList;
    }

    public void setFromSettings(Plugin.FramePluginSettings settings)
    {
        globalTags = settings.globalTags;
        globalGrammarTags = settings.grammarMap;
    }

    public void setTagsFromFile(File tagFile)
    {
        tags = tagFile;
        TagIO.jsonToTags(tags, grammarTags);
    }

    public void setGlobalTagsFromFile(File tagFile)
    {
        globalTags = tagFile;
        TagIO.jsonToTags(globalTags, grammarTags);
    }

    public void setID(String ID)
    {
        this.ID = ID;
    }

    public String getID()
    {
        return ID;
    }

    public void addSlot(SlotStruct slot)
    {

        slotList.add(slot);
    }

    public SlotStruct getSlot(int index)
    {
        return slotList.get(index);
    }

    public List<SlotStruct> getSlots()
    {
        return slotList;
    }

    public boolean isFilled()
    {
        if (isEmpty())
        {
            return false;
        }

        for (SlotStruct slot : slotList)
        {
            if (!slot.isComplete())
            {
                return false;
            }
        }

        return true;
    }

    public void removeSlot(int index)
    {
        slotList.remove(index);
    }

    public HashMap<String, String> getAllGrammars()
    {
        HashMap<String, String> combined = new HashMap<>(grammarTags);
        if (globalGrammarTags != null)
        {
            combined.putAll(globalGrammarTags);
        }
        return combined;
    }

    public int size()
    {
        return slotList.size();
    }

    public boolean isEmpty()
    {
        return slotList.isEmpty();
    }

    /**
     * Sort the Slot list of the frame, by whether or not the slot is filled.
     * Unfilled slots come first.
     */
    public List<SlotStruct> sortFilled()
    {
        List<SlotStruct> temp = new ArrayList<>(slotList);
        temp.sort(slotComparator.reversed());
        return temp;
    }

    @Override
    public JSONObject marshal()
    {
        return null;
    }

    @Override
    public boolean unmarshal(JSONObject jsonObject)
    {
        return false;
    }

    @Override
    public JSONObject marshalStruct()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", ID);

        jsonObject.put("TAG_FILE", tags.getAbsolutePath());
        jsonObject.put("GRAMMAR_TAGS", new JSONObject(grammarTags));

        JSONArray slotArray = new JSONArray();
        for (SlotStruct slot : slotList)
        {
            slotArray.put(slot.marshal());
        }
        jsonObject.put("SLOT_LIST", slotArray);

        return jsonObject;
    }

    @Override
    public boolean unmarshalStruct(JSONObject jsonObject)
    {
        for (String key : new String[]{"ID", "TAG_FILE", "GRAMMAR_TAGS", "SLOT_LIST"})
        {
            if (!jsonObject.has(key))
            {
                return false;
            }
        }

        ID = jsonObject.getString("ID");
        tags = new File(jsonObject.getString("TAG_FILE"));

        grammarTags = new HashMap<>();
        for (String key : jsonObject.getJSONObject("GRAMMAR_TAGS").keySet())
        {
            grammarTags.put(key, jsonObject.getJSONObject("GRAMMAR_TAGS").getString(key));
        }

        slotList = new ArrayList<>();
        for (int inx = 0; inx < jsonObject.getJSONArray("SLOT_LIST").length(); inx++)
        {
            SlotStruct slot = new SlotStruct();
            if (!slot.unmarshalStruct(jsonObject.getJSONArray("SLOT_LIST").getJSONObject(inx)))
            {
                return false;
            }
            slotList.add(inx, slot);
        }

        return true;
    }
}
