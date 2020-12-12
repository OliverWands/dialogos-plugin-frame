package dialogos.frame;

import com.clt.diamant.IdentityObject;
import com.clt.xml.AbstractHandler;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.tags.TagIO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.Attributes;

import java.io.File;
import java.util.*;

public class FrameStruct implements Marshalling, IdentityObject
{
    private String id;
    private String name = null;
    private File grammarFile = null;
    private String helpPrompt = null;
    private List<SlotStruct> slotList = new ArrayList<>();
    private HashMap<String, String> grammars = new HashMap<>();

    private final Comparator<SlotStruct> slotComparator =
            (o1, o2) -> Boolean.compare(!o1.isFilled(), !o2.isFilled());

    public FrameStruct()
    {
        id = UUID.randomUUID().toString();
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

    public void setSlots(List<SlotStruct> slots)
    {
        slotList = slots;
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
        return name != null && !slotList.isEmpty() && !grammars.isEmpty() && helpPrompt != null;
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("NAME", name);
        jsonObject.put("TAG_FILE", grammarFile != null ? grammarFile.getAbsolutePath() : "");
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
    public boolean unmarshal(JSONObject jsonObject)
    {
        for (String key : new String[]{"NAME", "TAG_FILE", "GRAMMAR_TAGS", "SLOT_LIST", "HELP_PROMPT"})
        {
            if (!jsonObject.has(key))
            {
                return false;
            }
        }

        name = jsonObject.getString("NAME");
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
            if (!slot.unmarshal(jsonObject.getJSONArray("SLOT_LIST").getJSONObject(inx)))
            {
                return false;
            }
            slotList.add(inx, slot);
        }

        return true;
    }

    @Override
    public void marshalXML(XMLWriter writer)
    {
        if (this.isEdited())
        {
            writer.openElement("frameStruct",
                    new String[]{"uid", "name", "helpPrompt", "class"},
                    new Object[]{getId(), getName(), getHelpPrompt(), getClass().getName()});

            for (SlotStruct slotStruct : slotList)
            {
                slotStruct.marshalXML(writer);
            }

            writer.closeElement("frameStruct");
        }
    }

    public void unmarshalXML(XMLReader reader)
    {
        reader.setHandler(new AbstractHandler("frameStruct")
        {
            @Override
            protected void start(String name, Attributes atts)
            {
                switch (name)
                {
                    case "frameStruct":
                        setId(atts.getValue("uid"));
                        setName(atts.getValue("name"));
                        setHelpPrompt(atts.getValue("helpPrompt"));
                        break;
                    case "slotStruct":
                        reader.setHandler(new AbstractHandler("slotStruct"));
                        SlotStruct slotStruct = new SlotStruct();
                        slotStruct.setId(atts.getValue("uid"));
                        slotStruct.setName(atts.getValue("name"));
                        slotStruct.setQuery(atts.getValue("query"));
                        slotStruct.setGrammarName(atts.getValue("grammarName"));

                        addSlot(slotStruct);
                        break;
                }

            }
        });
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }
}
