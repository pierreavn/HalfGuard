package org.jnbt;




























public final class FloatTag
  extends Tag
{
  private final float value;
  


























  public FloatTag(String name, float value)
  {
    super(name);
    this.value = value;
  }
  

  public Float getValue()
  {
    return Float.valueOf(value);
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Float" + append + ": " + value;
  }
  





  public int hashCode()
  {
    //int prime = 31;
    int result = super.hashCode();
    result = 31 * result + Float.floatToIntBits(value);
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof FloatTag)) return false;
    //FloatTag other = (FloatTag)obj;
    if (Float.floatToIntBits(value) != Float.floatToIntBits(value)) return false;
    return true;
  }
}
