package org.jnbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;































































public final class NBTOutputStream
  implements Closeable
{
  private final DataOutputStream os;
  
  public NBTOutputStream(OutputStream os)
    throws IOException
  {
    this.os = new DataOutputStream(new GZIPOutputStream(os));
  }
  










  public NBTOutputStream(OutputStream os, boolean gzipped)
    throws IOException
  {
    if (gzipped) {
      os = new GZIPOutputStream(os);
    }
    this.os = new DataOutputStream(os);
  }
  







  public void writeTag(Tag tag)
    throws IOException
  {
    int type = NBTUtils.getTypeCode(tag.getClass());
    String name = tag.getName();
    byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
    
    os.writeByte(type);
    os.writeShort(nameBytes.length);
    os.write(nameBytes);
    
    if (type == 0) {
      throw new IOException("[JNBT] Named TAG_End not permitted.");
    }
    writeTagPayload(tag);
  }
  







  private void writeTagPayload(Tag tag)
    throws IOException
  {
    int type = NBTUtils.getTypeCode(tag.getClass());
    switch (type)
    {
    case 0: 
      writeEndTagPayload((EndTag)tag);
      break;
    case 1: 
      writeByteTagPayload((ByteTag)tag);
      break;
    case 2: 
      writeShortTagPayload((ShortTag)tag);
      break;
    case 3: 
      writeIntTagPayload((IntTag)tag);
      break;
    case 4: 
      writeLongTagPayload((LongTag)tag);
      break;
    case 5: 
      writeFloatTagPayload((FloatTag)tag);
      break;
    case 6: 
      writeDoubleTagPayload((DoubleTag)tag);
      break;
    case 7: 
      writeByteArrayTagPayload((ByteArrayTag)tag);
      break;
    case 8: 
      writeStringTagPayload((StringTag)tag);
      break;
    case 9: 
      writeListTagPayload((ListTag)tag);
      break;
    case 10: 
      writeCompoundTagPayload((CompoundTag)tag);
      break;
    case 11: 
      writeIntArrayTagPayload((IntArrayTag)tag);
      break;
    default: 
      throw new IOException("[JNBT] Invalid tag type: " + type + 
        ".");
    }
    
  }
  






  private void writeByteTagPayload(ByteTag tag)
    throws IOException
  {
    os.writeByte(tag.getValue().byteValue());
  }
  







  private void writeByteArrayTagPayload(ByteArrayTag tag)
    throws IOException
  {
    byte[] bytes = tag.getValue();
    os.writeInt(bytes.length);
    os.write(bytes);
  }
  







  private void writeCompoundTagPayload(CompoundTag tag)
    throws IOException
  {
    for (Tag childTag : tag.getValue().values()) {
      writeTag(childTag);
    }
    os.writeByte(0);
  }
  







  private void writeListTagPayload(ListTag tag)
    throws IOException
  {
    Class<? extends Tag> clazz = tag.getType();
    List<Tag> tags = tag.getValue();
    int size = tags.size();
    
    os.writeByte(NBTUtils.getTypeCode(clazz));
    os.writeInt(size);
    for (int i = 0; i < size; i++) {
      writeTagPayload((Tag)tags.get(i));
    }
  }
  







  private void writeStringTagPayload(StringTag tag)
    throws IOException
  {
    byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
    os.writeShort(bytes.length);
    os.write(bytes);
  }
  







  private void writeDoubleTagPayload(DoubleTag tag)
    throws IOException
  {
    os.writeDouble(tag.getValue().doubleValue());
  }
  







  private void writeFloatTagPayload(FloatTag tag)
    throws IOException
  {
    os.writeFloat(tag.getValue().floatValue());
  }
  







  private void writeLongTagPayload(LongTag tag)
    throws IOException
  {
    os.writeLong(tag.getValue().longValue());
  }
  







  private void writeIntTagPayload(IntTag tag)
    throws IOException
  {
    os.writeInt(tag.getValue().intValue());
  }
  







  private void writeShortTagPayload(ShortTag tag)
    throws IOException
  {
    os.writeShort(tag.getValue().shortValue());
  }
  







  private void writeIntArrayTagPayload(IntArrayTag tag)
    throws IOException
  {
    int[] ints = tag.getValue();
    os.writeInt(ints.length);
    for (int i = 0; i < ints.length; i++) {
      os.writeInt(ints[i]);
    }
  }
  






  private void writeEndTagPayload(EndTag tag) {}
  





  public void close()
    throws IOException
  {
    os.close();
  }
}
