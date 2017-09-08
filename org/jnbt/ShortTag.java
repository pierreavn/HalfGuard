package org.jnbt;




























public final class ShortTag
  extends Tag
{
  private final short value;
  


























  public ShortTag(String name, short value)
  {
    super(name);
    this.value = value;
  }
  

  public Short getValue()
  {
    return Short.valueOf(value);
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Short" + append + ": " + value;
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
    if (!(obj instanceof ShortTag)) return false;
    //ShortTag other = (ShortTag)obj;
    //if (value != value) return false;
    return true;
  }
}
