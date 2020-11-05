package dialogos.frame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FrameStruct
{
    private String ID;
    private List<SlotStruct> slotList = new ArrayList<>();

    private HashMap<String, String> globalDefinedTags = new HashMap<>();
    private HashMap<String, String> globalRegexTags = new HashMap<>();

    private HashMap<String, String> definedTags = new HashMap<>();
    private HashMap<String, String> regexTags = new HashMap<>();

    public FrameStruct()
    {
        this(null);
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

    //
    // TODO implement collection of sorting methods. That retains the original order.
    //
    public void sortBy()
    {

    }

    public void sortByName()
    {

    }

    public void sortByFilled()
    {

    }

    public int size()
    {
        return slotList.size();
    }

    public boolean isEmpty()
    {
        return slotList.isEmpty();
    }
}
