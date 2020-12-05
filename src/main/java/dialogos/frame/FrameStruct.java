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
    private String name = null;
    private File grammarFile = null;
    private String helpPrompt = null;
    private List<SlotStruct> slotList = new ArrayList<>();
    private HashMap<String, String> grammars = new HashMap<>();

    private final Comparator<SlotStruct> slotComparator =
            (o1, o2) -> Boolean.compare(!o1.isFilled(), !o2.isFilled());

    public FrameStruct()
    {
    }

    public FrameStruct(String name)
    {
        this.name = name;
    }

    public FrameStruct(String name, List<SlotStruct> slotList)
    {
        this.name = name;
        this.slotList = slotList;
    }

    public void setTagsFromFile(File tagFile)
    {
        grammarFile = tagFile;
        TagIO.jsonToTags(grammarFile, grammars);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addSlot(SlotStruct slot)
    {
        slotList.add(slot);
    }

    public void addSlot(int index, SlotStruct slot)
    {
        slotList.add(index, slot);
    }

    public void setSlot(int index, SlotStruct slot)
    {
        slotList.set(index, slot);
    }

    public SlotStruct getSlot(int index)
    {
        return slotList.get(index);
    }

    public List<SlotStruct> getSlots()
    {
        return slotList;
    }

    public int getIndex(SlotStruct slotStruct)
    {
        return slotList.indexOf(slotStruct);
    }

    public void setHelpPrompt(String helpPrompt)
    {
        this.helpPrompt = helpPrompt;
    }

    public String getHelpPrompt()
    {
        return helpPrompt;
    }

    public File getGrammarFile()
    {
        return grammarFile;
    }

    public boolean isFilled()
    {
        if (isEmpty())
        {
            return false;
        }

        for (SlotStruct slot : slotList)
        {
            if (!slot.isFilled())
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

    public HashMap<String, String> getGrammars()
    {
        return grammars;
    }

    public int size()
    {
        return slotList.size();
    }

    public boolean isEmpty()
    {
        return slotList.isEmpty();
    }

    public boolean isEdited()
    {
        return !(grammarFile == null || name == null || slotList.isEmpty()
                || grammars.isEmpty() || helpPrompt == null);
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
        jsonObject.put("ID", name);
        jsonObject.put("TAG_FILE", grammarFile.getAbsolutePath());
        jsonObject.put("GRAMMAR_TAGS", new JSONObject(grammars));
        jsonObject.put("HELP_PROMPT", helpPrompt);

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
        for (String key : new String[]{"ID", "TAG_FILE", "GRAMMAR_TAGS", "SLOT_LIST", "HELP_PROMPT"})
        {
            if (!jsonObject.has(key))
            {
                return false;
            }
        }

        name = jsonObject.getString("ID");
        grammarFile = new File(jsonObject.getString("TAG_FILE"));
        helpPrompt = jsonObject.getString("HELP_PROMPT");

        grammars = new HashMap<>();
        for (String key : jsonObject.getJSONObject("GRAMMAR_TAGS").keySet())
        {
            grammars.put(key, jsonObject.getJSONObject("GRAMMAR_TAGS").getString(key));
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
