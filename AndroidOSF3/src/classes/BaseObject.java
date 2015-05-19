package classes;

public class BaseObject
{
	public final String id;

	public BaseObject(String id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return String.format("BaseObject [id=%s]", id);
	}
}
