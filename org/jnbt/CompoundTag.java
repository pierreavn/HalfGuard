package org.jnbt;

import java.util.Collections;
import java.util.Map;





















































public final class CompoundTag
  extends Tag
{
  private final Map<String, Tag> value;
  
  public CompoundTag(String name, Map<String, Tag> value)
  {
    super(name);
    this.value = Collections.unmodifiableMap(value);
  }
  

  public Map<String, Tag> getValue()
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
    StringBuilder bldr = new StringBuilder();
    bldr.append("TAG_Compound" + append + ": " + value.size() + 
      " entries\r\n{\r\n");
    for (Map.Entry<String, Tag> entry : value.entrySet()) {
      bldr.append("   " + 
        ((Tag)entry.getValue()).toString().replaceAll("\r\n", "\r\n   ") + 
        "\r\n");
    }
    bldr.append("}");
    return bldr.toString();
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
    if (!(obj instanceof CompoundTag)) return false;
    //CompoundTag other = (CompoundTag)obj;
    if (value == null) {
      if (value != null) return false;
    } else if (!value.equals(value)) return false;
    return true;
  }
}
