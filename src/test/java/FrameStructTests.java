import dialogos.frame.FrameStruct;
import dialogos.frame.SlotStruct;
import dialogos.frame.utils.Token;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FrameStructTests
{
    @Test
    public void testCreateById()
    {
        String id = "Test_ID";
        FrameStruct frame = new FrameStruct(id);
        assert frame.isEmpty();
        assert frame.getID().equals(id);
        assert !frame.getID().equals("not id");
        assert !frame.isFilled();
    }

    @Test
    public void testCreateByIdAndList()
    {
        List<SlotStruct> slots = new ArrayList<>();
        slots.add(new SlotStruct("Slot1")
                .setMatchedTags(new String[]{"tag1", "tag2"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot1"));
        slots.add(new SlotStruct("Slot2")
                .setMatchedTags(new String[]{"tag3", "tag4"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot2"));
        slots.add(new SlotStruct("Slot3")
                .setMatchedTags(new String[]{"tag5", "tag6"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot3"));

        String id = "Test_ID";
        FrameStruct frame = new FrameStruct(id, slots);
        assert frame.size() == 3;
        assert !frame.isFilled();
        assert frame.getSlot(0).getName().equals("Slot1");

        frame.getSlot(0).setValue(new Token("Token1", 0, 1));
        frame.getSlot(1).setValue(new Token("Token2", 2, 3));
        frame.getSlot(2).setValue(new Token("Token3", 4, 5));

        // TODO Testing with matchTags necessary!
        // assert frame.isFilled();
    }
}
