package dialogos.frame;

import com.clt.diamant.Grammar;
import com.clt.diamant.IdentityObject;
import com.clt.xml.AbstractHandler;
import com.clt.xml.XMLReader;
import com.clt.xml.XMLWriter;
import dialogos.frame.utils.tags.GrammarIO;
import org.xml.sax.Attributes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class FrameStruct implements IdentityObject
{
    private String id;
    private String name = null;
    private File grammarFile = null;
    private String helpPrompt = null;
    private List<SlotStruct> slotList = new ArrayList<>();
    private List<Grammar> loadedGrammars = new ArrayList<>();
    private final List<Grammar> usedGrammars = new ArrayList<>();

    private final Comparator<SlotStruct> slotComparator =
            (o1, o2) -> Boolean.compare(!o1.isFilled(), !o2.isFilled());

    public FrameStruct()
    {
        setId(UUID.randomUUID().toString());
    }

    public void setGrammarsFromFile(File grammarFile)
    {
        if (grammarFile != this.grammarFile)
        {
            this.grammarFile = grammarFile;

            if (grammarFile.getName().endsWith("json"))
            {
                loadedGrammars = GrammarIO.jsonToGrammars(this.grammarFile);
            }
            else if (grammarFile.getName().endsWith("xml"))
            {
                loadedGrammars = GrammarIO.xmlToGrammars(this.grammarFile);
            }
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public List<Grammar> getUsedGrammars()
    {
        return usedGrammars;
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

    // TODO Move the Grammar file selector from the NewFrameMenu to the FrameNodeMenu.
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

    public List<Grammar> getLoadedGrammars()
    {
        return loadedGrammars;
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
        return name != null && !slotList.isEmpty() && !usedGrammars.isEmpty() && helpPrompt != null;
    }

    public List<SlotStruct> sortFilled()
    {
        List<SlotStruct> temp = new ArrayList<>(slotList);
        temp.sort(slotComparator.reversed());
        return temp;
    }

    public void writeToXML(XMLWriter writer)
    {
        if (this.isEdited())
        {
            writer.openElement("frameStruct",
                    new String[]{"uid", "name", "helpPrompt", "class"},
                    new Object[]{getId(), getName(), getHelpPrompt(), getClass().getName()});

            for (SlotStruct slotStruct : slotList)
            {
                slotStruct.writeToXML(writer);
            }

            for (Grammar grammar : this.usedGrammars)
            {
                writer.openElement("grammar", new String[]{"id"}, new Object[]{grammar.getId()});
                writer.printElement("name", grammar.getName());
                writer.printElement("value", grammar.getGrammar());
                writer.closeElement("grammar");
            }

            writer.closeElement("frameStruct");
        }
    }

    public void readFromXML(XMLReader reader)
    {
        reader.setHandler(getHandler(reader));
    }

    public void readFromXML(File file)
    {
        if (file != null)
        {
            XMLReader reader = new XMLReader(false);

            try
            {
                reader.parse(file, getHandler(reader));
            } catch (IOException exp)
            {
                exp.printStackTrace();
            }
        }
    }

    private AbstractHandler getHandler(XMLReader reader)
    {
        return new AbstractHandler("frameStruct")
        {
            @Override
            protected void start(String frameElem, Attributes atts)
            {
                switch (frameElem)
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
                    case "grammar":
                        final Grammar grammar = new Grammar("grammar");
                        grammar.setId(atts.getValue("id"));
                        loadedGrammars.add(grammar);
                        reader.setHandler(new AbstractHandler("grammar")
                        {
                            @Override
                            protected void end(String grammarElem)
                            {
                                if (grammarElem.equals("name"))
                                {
                                    grammar.setName(this.getValue());
                                }
                                else if (grammarElem.equals("value"))
                                {
                                    grammar.setGrammar(this.getValue());
                                }
                            }
                        });
                        break;
                }
            }
        };
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
