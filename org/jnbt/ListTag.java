package org.jnbt;

import java.util.Collections;
import java.util.List;




























































public final class ListTag
  extends Tag
{
  private final Class<? extends Tag> type;
  private final List<Tag> value;
  
  public ListTag(String name, Class<? extends Tag> type, List<Tag> value)
  {
    super(name);
    this.type = type;
    this.value = Collections.unmodifiableList(value);
  }
  





  public Class<? extends Tag> getType()
  {
    return type;
  }
  

  public List<Tag> getValue()
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
    bldr.append("TAG_List" + append + ": " + value.size() + 
      " entries of type " + NBTUtils.getTypeName(type) + 
      "\r\n{\r\n");
    for (Tag t : value) {
      bldr.append("   " + t.toString().replaceAll("\r\n", "\r\n   ") + 
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
    if (!(obj instanceof ListTag)) return false;
    //ListTag other = (ListTag)obj;
    if (value == null) {
      if (value != null) return false;
    } else if (!value.equals(value)) return false;
    return true;
  }
}
