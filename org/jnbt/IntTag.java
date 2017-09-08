package org.jnbt;




























public final class IntTag
  extends Tag
{
  private final int value;
  


























  public IntTag(String name, int value)
  {
    super(name);
    this.value = value;
  }
  

  public Integer getValue()
  {
    return Integer.valueOf(value);
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Int" + append + ": " + value;
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
    if (!(obj instanceof IntTag)) return false;
    //IntTag other = (IntTag)obj;
    //if (value != value) return false;
    return true;
  }
}
