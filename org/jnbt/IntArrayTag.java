package org.jnbt;

import java.util.Arrays;






















































public final class IntArrayTag
  extends Tag
{
  private final int[] value;
  
  public IntArrayTag(String name, int[] value)
  {
    super(name);
    this.value = value;
  }
  

  public int[] getValue()
  {
    return value;
  }
  

  public String toString()
  {
    StringBuilder integers = new StringBuilder();
    for (int b : value) {
      integers.append(b).append(" ");
    }
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Int_Array" + append + ": " + integers.toString();
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
    if (!(obj instanceof IntArrayTag)) return false;
    //IntArrayTag other = (IntArrayTag)obj;
    if (!Arrays.equals(value, value)) return false;
    return true;
  }
}
