import dialogos.frame.FrameStruct;
import dialogos.frame.SlotStruct;
import dialogos.frame.utils.tokens.Token;
import org.json.JSONObject;
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
        String id = "Test_ID";
        FrameStruct frame = new FrameStruct(id);
        assert frame.isEmpty();
        assert frame.getID().equals(id);
        assert !frame.getID().equals("not id");
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
        Token token2 = new Token("Token2", 2, 3).addTag("tag4");
        createdFrame.getSlot(0).setValue(token1);
        createdFrame.getSlot(1).setValue(token2);

        assert !createdFrame.isFilled();

        assert createdFrame.size() == 3;
        createdFrame.removeSlot(2);
        assert createdFrame.size() == 2;

        assert createdFrame.isFilled();

        createdFrame.addSlot(new SlotStruct("Slot2")
                .setMatchedTags(new String[]{"tag5", "tag6"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot2"));

        Token token3 = new Token("Token3", 4, 5).addTag("tag6");
        createdFrame.getSlot(2).setValue(token3);

        assert createdFrame.isFilled();
    }

    @Test
    public void testSorting()
    {
        FrameStruct createdFrame = createFrame();
        Token token1 = new Token("Token1", 0, 1).addTag("tag1");
        Token token3 = new Token("Token3", 4, 5).addTag("tag6");
        createdFrame.getSlot(0).setValue(token1);
        createdFrame.getSlot(1);
        createdFrame.getSlot(2).setValue(token3);

        List<SlotStruct> sortedSlots = createdFrame.sortFilled();

        assert createdFrame.getSlot(0).getName().equals("Slot0");
        assert sortedSlots.get(0).getName().equals("Slot1");
        assert createdFrame.getSlot(0).getName().equals("Slot0");
    }

    @Test
    public void testStructMarshalling()
    {
        FrameStruct newFrame = new FrameStruct();

        JSONObject original = createFrame().marshalStruct();

        boolean unmarshalStruct = newFrame.unmarshalStruct(original);
        assert unmarshalStruct;

        JSONObject copy = newFrame.marshalStruct();

        assert copy.toString().equals(original.toString());
    }

    private FrameStruct createFrame()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL tagFile = classLoader.getResource("tagging.json");
        assert tagFile != null;

        File file = new File(tagFile.getFile());

        List<SlotStruct> slots = new ArrayList<>();
        slots.add(new SlotStruct("Slot0")
                .setMatchedTags(new String[]{"tag1", "tag2"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot0"));
        slots.add(new SlotStruct("Slot1")
                .setMatchedTags(new String[]{"tag3", "tag4"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot1"));
        slots.add(new SlotStruct("Slot2")
                .setMatchedTags(new String[]{"tag5", "tag6"})
                .setIsAdditional(false)
                .setQuery("Please enter Slot2"));

        String id = "Test_ID";
        FrameStruct newFrame = new FrameStruct(id, slots);
        newFrame.setTagsFromFile(file);

        return newFrame;
    }
}
