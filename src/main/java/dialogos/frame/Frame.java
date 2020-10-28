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

    public Slot getSlot(int index)
    {
        return slotList.get(index);
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
