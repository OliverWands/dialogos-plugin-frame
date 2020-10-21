package dialogos.frame;

import java.util.ArrayList;
import java.util.List;

public class Frame
{
    private List<Slot> slotList;

    public Frame()
    {
        slotList = new ArrayList<>();
    }

    public Frame(List<Slot> slotList)
    {
        this.slotList = slotList;
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
}
