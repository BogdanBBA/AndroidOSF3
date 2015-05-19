package classes;

import org.joda.time.DateTime;

public class Week
{
	public final DateTime Monday;
	public final DateTime Sunday;

	public Week(DateTime monday)
	{
		this.Monday = new DateTime(monday.getYear(), monday.getMonthOfYear(), monday.getDayOfMonth(), 0, 0, 0);
		this.Sunday = monday.plusDays(7).minusSeconds(1);
	}

	public Week(String monday)
	{
		this(Utils.DecodeDate(monday));
	}

	public boolean DateIsIncluded(DateTime date)
	{
		return Monday.getMillis() <= date.getMillis() && date.getMillis() <= Sunday.getMillis();
	}

	private String FormatWeek(String separator, String start, String end)
	{
		return String.format("%s%s%s", start, separator, end);
	}

	public String FormatWeek(boolean prettyDates, boolean includeSpaceInSeparator)
	{
		String separator = includeSpaceInSeparator ? " - " : "-";
		String start = prettyDates ? Utils.EncodeDatePrettily(Monday) : Utils.EncodeDate(Monday);
		String end = prettyDates ? Utils.EncodeDatePrettily(Sunday) : Utils.EncodeDate(Sunday);
		return FormatWeek(separator, start, end);
	}

	@Override
	public String toString()
	{
		return String.format("Week [Monday=%s, Sunday=%s]", Utils.EncodeDate(Monday), Utils.EncodeDate(Sunday));
	}
}
