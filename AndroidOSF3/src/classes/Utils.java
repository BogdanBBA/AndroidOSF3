package classes;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

@SuppressLint("DefaultLocale")
public abstract class Utils
{
	public static final String[] MonthNames_Romanian = new String[] {"???", "Ian", "Feb", "Mar", "Apr", "Mai", "Iun", "Iul", "Aug", "Sept", "Oct", "Nov", "Dec"};
	public static final String[] DowNames_Romanian = new String[] {"???", "Luni", "Marți", "Miercuri", "Joi", "Vineri", "Sâmbătă", "Duminică"};

	public static int StringIndexInStringArray(String str, String[] array)
	{
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(str))
				return i;
		return -1;
	}

	public static String EncodeTime(TimeSpan time)
	{
		return String.format("%d:%02d", time.Hours(), time.Minutes());
	}

	public static TimeSpan DecodeTime(String time)
	{
		try
		{
			int h = Integer.parseInt(time.substring(0, time.indexOf(':')));
			int m = Integer.parseInt(time.substring(time.indexOf(':') + 1, time.length()));
			return new TimeSpan(h, m, 0);
		}
		catch (Exception E)
		{
			return new TimeSpan();
		}
	}

	//

	public static String EncodeDate(DateTime date)
	{
		return String.format("%d.%02d.%02d", date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
	}

	public static String EncodeDatePrettily(DateTime date)
	{
		return String.format("%d %s %d", date.getDayOfMonth(), Utils.MonthNames_Romanian[date.getMonthOfYear()], date.getYear());
	}

	public static DateTime DecodeDate(String date)
	{
		try
		{
			int y = Integer.parseInt(date.substring(0, 4));
			int M = Integer.parseInt(date.substring(5, 7));
			int d = Integer.parseInt(date.substring(8, 10));
			return new DateTime(y, M, d, 0, 0);
		}
		catch (Exception E)
		{
			return new DateTime(2014, 10, 1, 23, 59);
		}
	}

	//

	public static String EncodeWeekList(ArrayList<Week> weekList)
	{
		return "EncodeWeekList(ArrayList<Week> weekList) not implemented";
	}

	public static ArrayList<Week> DecodeWeekList(String weekList)
	{
		ArrayList<Week> result = new ArrayList<Week>();
		weekList = weekList.replaceAll(" ", "");
		String[] mondays = weekList.indexOf(';') != -1 ? weekList.split(";") : new String[] {weekList};
		for (String monday: mondays)
			result.add(new Week(Utils.DecodeDate(monday)));
		Utils.SortWeekList(result);
		return result;
	}

	public static void SortWeekList(ArrayList<Week> weeks)
	{
		for (int i = 0; i < weeks.size() - 1; i++)
			for (int j = i + 1; j < weeks.size(); j++)
				if (weeks.get(i).Monday.isAfter(weeks.get(j).Monday))
				{
					Week aux = weeks.get(i);
					weeks.set(i, weeks.get(j));
					weeks.set(j, aux);
				}
	}

	// // //

	public static DateTime GetMondayForDay(DateTime date)
	{
		while (date.dayOfWeek().get() != DateTimeConstants.MONDAY)
			date = date.plusDays(1);
		return date;
	}

	public static int IndexOfWeekInWeekList(Week targetWeek, ArrayList<Week> weekList)
	{
		for (int i = 0; i < weekList.size(); i++)
			if (targetWeek.Monday.getMillis() == weekList.get(i).Monday.getMillis())
				return i;
		return -1;
	}

	public static ArrayList<Class> GenerateClassesForValues(Discipline discipline, String when, ClassType classType, Professor professor, Room room, ArrayList<WeekCategory> weekCategories, Database database)
			throws PreventableErrorException
	{
		String phase = "initialization";
		try
		{
			ArrayList<Class> result = new ArrayList<Class>();

			when = when.replaceAll(" ", "");
			String[] combos = when.indexOf('|') != -1 ? when.split("\\|") : new String[] {when};
			for (String combo: combos) // impare+pare:1@C
			{
				phase = "combo \"" + combo + "\" *1";
				// get all involved weeks
				String weekCategsStr = combo.substring(0, combo.indexOf(':'));
				String[] weekCategs = weekCategsStr.indexOf('+') != -1 ? weekCategsStr.split("\\+") : new String[] {weekCategsStr};
				ArrayList<Week> weeks = new ArrayList<Week>();
				for (String weekCateg: weekCategs)
					for (WeekCategory weekCategory: weekCategories)
						if (weekCateg.equals(weekCategory.id))
							for (Week week: weekCategory.weeks)
								if (IndexOfWeekInWeekList(week, weeks) == -1)
									weeks.add(week);
				phase = "combo \"" + combo + "\" *2";
				// get day of week for those involved weeks
				int dayOfWeek = Integer.parseInt(combo.substring(combo.indexOf(':') + 1, combo.indexOf(':') + 2));
				// get time interval
				String timeIntervalID = combo.substring(combo.indexOf('@') + 1, combo.length());
				TimeInterval timeInterval = database.GetTimeIntervalByID(timeIntervalID);

				phase = "combo \"" + combo + "\" *3";
				// for all weeks involved, add a new class to the resultWeek
				for (Week week: weeks)
				{
					DateWithTimeInterval DwTI = new DateWithTimeInterval(week.Monday.plusDays(dayOfWeek - 1), timeInterval);
					result.add(new Class(discipline, classType, DwTI, room, professor));
				}
			}

			SortClassesChronologically(result);
			return result;
		}
		catch (Exception e)
		{
			throw new PreventableErrorException("GenerateClassesForValues(when=" + when + ") " + e.getMessage() + " ERROR, during " + phase);
		}
	}

	public static void SortClassesChronologically(ArrayList<Class> classes)
	{
		if (classes.size() < 2)
			return;
		for (int i = 0; i < classes.size() - 1; i++)
			for (int j = i + 1; j < classes.size(); j++)
			{
				DateWithTimeInterval c1 = classes.get(i).when, c2 = classes.get(j).when;
				if ((c1.date.getMillis() > c2.date.getMillis()) || (c1.date.getMillis() == c2.date.getMillis() && c1.timeInterval.start.TotalSeconds() > c2.timeInterval.start.TotalSeconds()))
				{
					Class aux = classes.get(i);
					classes.set(i, classes.get(j));
					classes.set(j, aux);
				}
			}
	}

	public static TimeInterval GetTimeIntervalForClasses(ArrayList<Class> classes)
	{
		if (classes.size() == 0)
			return new TimeInterval("", new TimeSpan(0), new TimeSpan(0));
		TimeInterval result = new TimeInterval("", new TimeSpan(23, 59, 59), new TimeSpan(0));
		for (Class cls: classes)
		{
			if (result.start.TotalMinutes() > cls.when.timeInterval.start.TotalMinutes())
				result.start = cls.when.timeInterval.start;
			if (result.end.TotalMinutes() < cls.when.timeInterval.end.TotalMinutes())
				result.end = cls.when.timeInterval.end;
		}
		return result;
	}

	public static int GetSemesterWeekMostRelevantToTodayPosition(Semester semester)
	{
		if (semester.Weeks.size() == 0 || semester.AllClasses.size() == 0)
			return -1;
		if (DateTime.now().getMillis() < semester.Weeks.get(0).Monday.getMillis())
			return 0;
		for (int iWeek = 0; iWeek < semester.Weeks.size(); iWeek++)
			if (DateTime.now().getMillis() < semester.Weeks.get(iWeek).Monday.plusDays(5).getMillis())
				return iWeek;
		return -1;
	}

	public static Week GetSemesterWeekMostRelevantToToday(Semester semester)
	{
		int weekPos = GetSemesterWeekMostRelevantToTodayPosition(semester);
		return weekPos != -1 ? semester.Weeks.get(weekPos) : null;
	}

	// // //

	// public static int DayOfWeekAsInt(DayOfWeek dow)
	// {
	// switch (dow)
	// {
	// case DayOfWeek.Monday:
	// return 1;
	// case DayOfWeek.Tuesday:
	// return 2;
	// case DayOfWeek.Wednesday:
	// return 3;
	// case DayOfWeek.Thursday:
	// return 4;
	// case DayOfWeek.Friday:
	// return 5;
	// case DayOfWeek.Saturday:
	// return 6;
	// case DayOfWeek.Sunday:
	// return 7;
	// default:
	// return 0;
	// }
	// }

	public static double GetTimeSpanRatio(TimeSpan time, TimeInterval interval)
	{
		if ((time.TotalSeconds() < interval.start.TotalSeconds() && time.TotalSeconds() > interval.end.TotalSeconds()) || (interval.start.equals(interval.end)))
			return 0;
		return (double) (time.TotalMinutes() - interval.start.TotalMinutes()) / (interval.end.TotalMinutes() - interval.start.TotalMinutes());
	}

}
