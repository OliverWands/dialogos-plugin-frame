import dialogos.frame.struct.SlotStruct;
import org.junit.Test;

public class SlotStructTests
{
    @Test
    public void testSlotStruct()
    {
        SlotStruct slotStruct = new SlotStruct();
        assert !slotStruct.isFilled();
    }
}
