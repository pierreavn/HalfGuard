package org.jnbt;



























public abstract class Tag
{
  private final String name;
  


























  public Tag(String name)
  {
    this.name = name;
  }
  





  public final String getName()
  {
    return name;
  }
  





  public abstract Object getValue();
  





  public int hashCode()
  {
    //int prime = 31;
    int result = 1;
    result = 31 * result + (name == null ? 0 : name.hashCode());
    return result;
  }
  





  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Tag)) return false;
    //Tag other = (Tag)obj;
    if (name == null) {
      if (name != null) return false;
    } else if (!name.equals(name)) return false;
    return true;
  }
}
