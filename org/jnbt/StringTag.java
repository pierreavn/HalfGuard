package org.jnbt;




























public final class StringTag
  extends Tag
{
  private final String value;
  


























  public StringTag(String name, String value)
  {
    super(name);
    this.value = value;
  }
  

  public String getValue()
  {
    return value;
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_String" + append + ": " + value;
  }
  





  public int hashCode()
  {
    //int prime = 31;
    int result = super.hashCode();
    result = 31 * result + (value == null ? 0 : value.hashCode());
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof StringTag)) return false;
    //StringTag other = (StringTag)obj;
    if (value == null) {
      if (value != null) return false;
    } else if (!value.equals(value)) return false;
    return true;
  }
}
