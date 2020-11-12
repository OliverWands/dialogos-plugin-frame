import dialogos.frame.SlotStruct;
import org.junit.Test;

public class SlotStructTests
{
    @Test
    public void testSlotStruct()
    {
        SlotStruct slotStruct = new SlotStruct();
        assert slotStruct.isNotFilled();
    }
}
