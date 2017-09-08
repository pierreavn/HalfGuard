package org.jnbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

































































public final class NBTInputStream
  implements Closeable
{
  private final DataInputStream is;
  
  public NBTInputStream(InputStream is, boolean gzipped)
    throws IOException
  {
    if (gzipped) {
      is = new GZIPInputStream(is);
    }
    this.is = new DataInputStream(is);
  }
  







  public NBTInputStream(InputStream is)
    throws IOException
  {
    this.is = new DataInputStream(new GZIPInputStream(is));
  }
  

  public NBTInputStream(DataInputStream is)
  {
    this.is = is;
  }
  






  public Tag readTag()
    throws IOException
  {
    return readTag(0);
  }
  








  private Tag readTag(int depth)
    throws IOException
  {
    int type = is.readByte() & 0xFF;
    String name;
    if (type != 0) {
      int nameLength = is.readShort() & 0xFFFF;
      byte[] nameBytes = new byte[nameLength];
      is.readFully(nameBytes);
      name = new String(nameBytes, NBTConstants.CHARSET);
    } else {
      name = "";
    }
    
    return readTagPayload(type, name, depth);
  }
  














  private Tag readTagPayload(int type, String name, int depth)
    throws IOException
  {
    switch (type)
    {
    case 0: 
      if (depth == 0) {
        throw new IOException(
          "[JNBT] TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
      }
      return new EndTag();
    
    case 1: 
      return new ByteTag(name, is.readByte());
    case 2: 
      return new ShortTag(name, is.readShort());
    case 3: 
      return new IntTag(name, is.readInt());
    case 4: 
      return new LongTag(name, is.readLong());
    case 5: 
      return new FloatTag(name, is.readFloat());
    case 6: 
      return new DoubleTag(name, is.readDouble());
    case 7: 
      int length = is.readInt();
      byte[] bytes = new byte[length];
      is.readFully(bytes);
      return new ByteArrayTag(name, bytes);
    case 8: 
      length = is.readShort();
      bytes = new byte[length];
      is.readFully(bytes);
      return new StringTag(name, new String(bytes, 
        NBTConstants.CHARSET));
    case 9: 
      int childType = is.readByte();
      length = is.readInt();
      
      @SuppressWarnings({ "rawtypes", "unchecked" }) List<Tag> tagList = new ArrayList();
      for (int i = 0; i < length; i++) {
        Tag tag = readTagPayload(childType, "", depth + 1);
        if ((tag instanceof EndTag))
          throw new IOException("[JNBT] TAG_End not permitted in a list.");
        tagList.add(tag);
      }
      
      return new ListTag(name, NBTUtils.getTypeClass(childType), 
        tagList);
    case 10: 
      @SuppressWarnings({ "rawtypes", "unchecked" }) Map<String, Tag> tagMap = new HashMap();
      for (;;) {
        Tag tag = readTag(depth + 1);
        if ((tag instanceof EndTag)) {
          break;
        }
        tagMap.put(tag.getName(), tag);
      }
      

      return new CompoundTag(name, tagMap);
    case 11: 
      length = is.readInt();
      int[] ints = new int[length];
      for (int i = 0; i < length; i++) {
        ints[i] = is.readInt();
      }
      return new IntArrayTag(name, ints);
    }
    throw new IOException("[JNBT] Invalid tag type: " + type + 
      ".");
  }
  

  public void close()
    throws IOException
  {
    is.close();
  }
}
