package dialogos.frame;

import java.util.ArrayList;
import java.util.List;

public class FrameStruct
{
    private String ID;
    private final List<SlotStruct> slotList;

    public FrameStruct(String ID)
    {
        this.ID = ID;
        slotList = new ArrayList<>();
    }

    public FrameStruct(String ID, List<SlotStruct> slotList)
    {
        this.ID = ID;
        this.slotList = slotList;
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
