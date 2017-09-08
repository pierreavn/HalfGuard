package org.jnbt;














































public final class EndTag
  extends Tag
{
  private final Object value = null;
  




  public EndTag()
  {
    super("");
  }
  

  public Object getValue()
  {
    return value;
  }
  

  public String toString()
  {
    return "TAG_End";
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
    if (!(obj instanceof EndTag)) return false;
    //EndTag other = (EndTag)obj;
    if (value == null) {
      if (value != null) return false;
    } else if (!value.equals(value)) return false;
    return true;
  }
}
