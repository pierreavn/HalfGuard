package org.jnbt;




























public final class DoubleTag
  extends Tag
{
  private final double value;
  


























  public DoubleTag(String name, double value)
  {
    super(name);
    this.value = value;
  }
  

  public Double getValue()
  {
    return Double.valueOf(value);
  }
  

  public String toString()
  {
    String name = getName();
    String append = "";
    if ((name != null) && (!name.equals(""))) {
      append = "(\"" + getName() + "\")";
    }
    return "TAG_Double" + append + ": " + value;
  }
  





  public int hashCode()
  {
    //int prime = 31;
    int result = super.hashCode();
    
    long temp = Double.doubleToLongBits(value);
    result = 31 * result + (int)(temp ^ temp >>> 32);
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof DoubleTag)) return false;
    //DoubleTag other = (DoubleTag)obj;
    if (Double.doubleToLongBits(value) != 
      Double.doubleToLongBits(value)) return false;
    return true;
  }
}
