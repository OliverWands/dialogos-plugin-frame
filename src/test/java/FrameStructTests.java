import dialogos.frame.struct.FrameStruct;
import dialogos.frame.struct.SlotStruct;
import dialogos.frame.utils.Token;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FrameStructTests
{
    @Test
    public void testCreateById()
    {
        String name = "Test_ID";
        FrameStruct frame = new FrameStruct();
        frame.setName(name);
        assert frame.isEmpty();
        assert frame.getName().equals(name);
        assert !frame.getName().equals("not name");
        assert !frame.isFilled();
    }

    @Test
    public void testOperations()
    {
        FrameStruct createdFrame = new FrameStruct();
        assert createdFrame.isEmpty();

        createdFrame = createFrame();

        assert createdFrame.size() == 3;
        assert !createdFrame.isFilled();
        assert createdFrame.getSlot(0).getName().equals("Slot0");

        Token token1 = new Token("Token1", 0, 1).addTag("tag1");
        Token token2 = new Token("Token2", 2, 3).addTag("tag2");
        createdFrame.getSlot(0).setTokenValue(token1);
        createdFrame.getSlot(1).setTokenValue(token2);

        assert !createdFrame.isFilled();

        assert createdFrame.size() == 3;
        createdFrame.removeSlot(2);
        assert createdFrame.size() == 2;

        assert createdFrame.isFilled();

        createdFrame.addSlot(new SlotStruct()
                                     .setName("Slot3")
                                     .setGrammarName("tag3")
                                     .setQuery("Please enter Slot3"));

        Token token3 = new Token("Token3", 4, 5).addTag("tag3");
        createdFrame.getSlot(2).setTokenValue(token3);

        assert createdFrame.isFilled();
    }

    @Test
    public void testSorting()
    {
        FrameStruct createdFrame = createFrame();
        Token token1 = new Token("Token1", 0, 1).addTag("tag1");
        Token token3 = new Token("Token3", 4, 5).addTag("tag2");
        createdFrame.getSlot(0).setTokenValue(token1);
        createdFrame.getSlot(1);
        createdFrame.getSlot(2).setTokenValue(token3);

        List<SlotStruct> sortedSlots = createdFrame.sortFilled();

        assert createdFrame.getSlot(0).getName().equals("Slot0");
        assert sortedSlots.get(0).getName().equals("Slot1");
        assert createdFrame.getSlot(0).getName().equals("Slot0");
    }

    private FrameStruct createFrame()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL tagFile = classLoader.getResource("tagging.json");
        assert tagFile != null;

        File file = new File(tagFile.getFile());

        List<SlotStruct> slots = new ArrayList<>();
        slots.add(new SlotStruct()
                          .setName("Slot0")
                          .setGrammarName("tag1")
                          .setQuery("Please enter Slot0"));
        slots.add(new SlotStruct()
                          .setName("Slot1")
                          .setGrammarName("tag2")
                          .setQuery("Please enter Slot1"));
        slots.add(new SlotStruct()
                          .setName("Slot2")
                          .setGrammarName("tag3")
                          .setQuery("Please enter Slot2"));

        String name = "Test_ID";
        FrameStruct newFrame = new FrameStruct();
        newFrame.setName(name);
        newFrame.setSlots(slots);
        newFrame.setGrammarsFromFile(file);

        return newFrame;
    }
}
