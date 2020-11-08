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
    FrameStruct frame;

    public FrameStructTests()
    {
        ClassLoader classLoader = getClass().getClassLoader();
        URL tagFile = classLoader.getResource("tagging.json");
        assert tagFile != null;

        File file = new File(tagFile.getFile());
        System.out.println(file.getAbsolutePath());

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
        frame = new FrameStruct(id, slots);
        frame.setTagsFromFile(file);
    }

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
        FrameStruct createdFrame = frame;
        assert createdFrame.size() == 3;
        assert !createdFrame.isFilled();
        assert createdFrame.getSlot(0).getName().equals("Slot1");

        createdFrame.getSlot(0).setValue(new Token("Token1", 0, 1));
        createdFrame.getSlot(1).setValue(new Token("Token2", 2, 3));
        createdFrame.getSlot(2).setValue(new Token("Token3", 4, 5));

        //assert frame.isFilled();
    }

    @Test
    public void testMarshalling()
    {
        FrameStruct newFrame = new FrameStruct();

        JSONObject original = frame.marshal();

        boolean unmarshal = newFrame.unmarshal(original);
        assert unmarshal;

        JSONObject copy = newFrame.marshal();

        assert copy.toString().equals(original.toString());
    }
}
