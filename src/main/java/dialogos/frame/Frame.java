package dialogos.frame;

import java.util.ArrayList;
import java.util.List;

public class Frame
{
    private String ID;
    private final List<Slot> slotList;

    public Frame(String ID)
    {
        this.ID = ID;
        slotList = new ArrayList<>();
    }

    public Frame(List<Slot> slotList)
    {
        this.slotList = slotList;
    }

    public String getID()
    {
        return ID;
    }

    public void addSlot(Slot slot)
    {

        slotList.add(slot);
    }

    public boolean isFilled()
    {
        for (Slot slot : slotList)
        {
            if (!slot.isComplete())
            {
                return false;
            }
        }
        return true;
    }

    //
    // TODO implement collection of sorting methods.
    //
    public void sortBy()
    {

    }
}
