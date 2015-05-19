package classes;

import org.joda.time.DateTime;

public class TimeSpan
{
	private final int totalSeconds;

	public TimeSpan(DateTime start, DateTime end)
	{
		if (start.getHourOfDay() * 60 + start.getMinuteOfDay() > end.getHourOfDay() * 60 + end.getMinuteOfDay())
		{
			DateTime aux = start;
			start = end;
			end = aux;
		}
		start = new DateTime(2014, 10, 1, start.getHourOfDay(), start.getMinuteOfDay());
		end = new DateTime(2014, 10, 1, end.getHourOfDay(), end.getMinuteOfDay());
		this.totalSeconds = end.getSecondOfDay() - start.getSecondOfDay();
	}

	public TimeSpan(int nSeconds)
	{
		this.totalSeconds = nSeconds;
	}

	public TimeSpan(int nHr, int nMin, int nSec)
	{
		this(nHr * 3600 + nMin * 60 + nSec);
	}

	public TimeSpan()
	{
		this(0);
	}

	public int Hours()
	{
		return this.totalSeconds / 3600;
	}

	public int Minutes()
	{
		return (this.totalSeconds % 3600) / 60;
	}

	public int Seconds()
	{
		return this.totalSeconds % 3600;
	}

	public double TotalHours()
	{
		return this.totalSeconds / 3600.0;
	}

	public double TotalMinutes()
	{
		return this.totalSeconds / 60.0;
	}

	public int TotalSeconds()
	{
		return this.totalSeconds;
	}

	@Override
	public String toString()
	{
		return String.format("TimeSpan [totalSeconds=%s]", totalSeconds);
	}
}
