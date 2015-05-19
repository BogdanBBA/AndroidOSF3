package classes;

public class Room extends NamedObject
{
	public Room(String id, String name)
	{
		super(id, name);
	}

	@Override
	public String toString()
	{
		return String.format("Room [%s]", super.toString());
	}
}
