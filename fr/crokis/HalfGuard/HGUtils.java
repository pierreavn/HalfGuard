package fr.crokis.HalfGuard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jnbt.CompoundTag;
import org.jnbt.ListTag;
import org.jnbt.Tag;

public class HGUtils {
	public boolean bool(int value) {
		if(value == 1)
			return true;
		
		return false;
	}
	
	public Map<String, Tag> copy(CompoundTag tNBT) {
		Map<String, Tag> tNBTValue = tNBT.getValue();
        Map<String, Tag> output = new HashMap<String, Tag>();
        for(String key : tNBTValue.keySet()) {
        		output.put(key, tNBTValue.get(key));
        }
        return output;
	}
	
	public List<Tag> copy(ListTag tNBT) {
		List<Tag> tNBTValue = tNBT.getValue();
        List<Tag> output = new ArrayList<Tag>();
        for(Tag tag : tNBTValue) {
        		output.add(tag);
        }
        return output;
	}
}
