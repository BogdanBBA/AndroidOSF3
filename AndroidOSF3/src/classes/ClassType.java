package classes;

public class ClassType extends NamedObject
{
	// should change this to "brush" if and when possible
	public final int color; 

	public ClassType(String id, String name, int textColor)
	{
		super(id, name);
		this.color = textColor;
	}

	@Override
	public String toString()
	{
		return String.format("ClassType [%s, textBrCol=%s]", super.toString(), color);
	}
}
