package classes;

public class NamedObject extends BaseObject
{
	public final String name;

	public NamedObject(String id, String name)
	{
		super(id);
		this.name = name;
	}
	
	@Override
	public String toString()
	{
		return String.format("NamedObject [%s, name=%s]", super.toString(), name);
	}
}
