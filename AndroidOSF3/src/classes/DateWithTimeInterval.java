package classes;

import org.joda.time.DateTime;

public class DateWithTimeInterval
{
	public final DateTime date;
	public final TimeInterval timeInterval;

	public DateWithTimeInterval(DateTime date, TimeInterval timeInterval)
	{
		this.date = date;
		this.timeInterval = timeInterval;
	}

	@Override
	public String toString()
	{
		return String.format("DateWithTimeInterval [date=%s, timeInterval=%s]", date, timeInterval);
	}
}
