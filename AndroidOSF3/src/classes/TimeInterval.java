package classes;

public class TimeInterval extends BaseObject
{
	public TimeSpan start;
	public TimeSpan end;

	public TimeInterval(String id, TimeSpan start, TimeSpan end)
	{
		super(id);
		this.start = start;
		this.end = end;
	}

	public TimeInterval(String id, String start, String end)
	{
		this(id, Utils.DecodeTime(start), Utils.DecodeTime(end));
	}

	public int LengthInMinutes()
	{
		return (int) new TimeSpan(end.TotalSeconds() - start.TotalSeconds()).TotalMinutes();
	}

	public String FormatInterval(boolean includeSpaceInSeparator)
	{
		String separator = includeSpaceInSeparator ? " - " : "-";
		return Utils.EncodeTime(start) + separator + Utils.EncodeTime(end);
	}

	@Override
	public String toString()
	{
		return String.format("TimeInterval [%s, start=%s, end=%s]", super.toString(), start, end);
	}
}
