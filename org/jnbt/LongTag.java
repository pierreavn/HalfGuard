package org.jnbt;




























public final class LongTag
  extends Tag
{
  private final long value;
  


























  public LongTag(String name, long value)
  {
    super(name);
    this.value = value;
  }
  

  public Long getValue()
  {
    return Long.valueOf(value);
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Long" + append + ": " + value;
  }
  





  public int hashCode()
  {
    //int prime = 31;
    int result = super.hashCode();
    result = 31 * result + (int)(value ^ value >>> 32);
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof LongTag)) return false;
    //LongTag other = (LongTag)obj;
    //if (value != value) return false;
    return true;
  }
}
