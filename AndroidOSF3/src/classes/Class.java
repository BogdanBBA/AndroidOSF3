package classes;

public class Class
{
	public final Discipline discipline;
	public final ClassType classType;
	public final DateWithTimeInterval when;
	public final Room where;
	public final Professor whoWith;

	public Class(Discipline discipline, ClassType classType, DateWithTimeInterval when, Room where, Professor whoWith)
	{
		this.discipline = discipline;
		this.classType = classType;
		this.when = when;
		this.where = where;
		this.whoWith = whoWith;
	}

	@Override
	public String toString()
	{
		return String.format("Class [%s, discipline=%s, classType=%s, when=%s, where=%s, whoWith=%s]", super.toString(), discipline, classType, when, where, whoWith);
	}

}
