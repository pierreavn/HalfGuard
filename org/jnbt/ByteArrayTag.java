package org.jnbt;

import java.util.Arrays;






















































public final class ByteArrayTag
  extends Tag
{
  private final byte[] value;
  
  public ByteArrayTag(String name, byte[] value)
  {
    super(name);
    this.value = value;
  }
  

  public byte[] getValue()
  {
    return value;
  }
  

  public String toString()
  {
    StringBuilder hex = new StringBuilder();
    for (byte b : value) {
      String hexDigits = Integer.toHexString(b).toUpperCase();
      if (hexDigits.length() == 1) {
        hex.append("0");
      }
      hex.append(hexDigits).append(" ");
    }
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Byte_Array" + append + ": " + hex.toString();
  }
  





  public int hashCode()
  {
    //int prime = 31;
    int result = super.hashCode();
    result = 31 * result + Arrays.hashCode(value);
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof ByteArrayTag)) return false;
    //ByteArrayTag other = (ByteArrayTag)obj;
    if (!Arrays.equals(value, value)) return false;
    return true;
  }
}
