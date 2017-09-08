package org.jnbt;




























public final class ByteTag
  extends Tag
{
  private final byte value;
  


























  public ByteTag(String name, byte value)
  {
    super(name);
    this.value = value;
  }
  

  public Byte getValue()
  {
    return Byte.valueOf(value);
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Byte" + append + ": " + value;
  }
  





  public int hashCode()
  {
    //int prime = 31;
    int result = super.hashCode();
    result = 31 * result + value;
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof ByteTag)) return false;
    //ByteTag other = (ByteTag)obj;
    //if (value != value) return false;
    return true;
  }
}
