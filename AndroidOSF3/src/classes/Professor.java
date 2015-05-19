package classes;

public class Professor extends NamedObject
{
	public Professor(String id, String name)
	{
		super(id, name);
	}

	@Override
	public String toString()
	{
		return String.format("Professor [%s]", super.toString());
	}
}
