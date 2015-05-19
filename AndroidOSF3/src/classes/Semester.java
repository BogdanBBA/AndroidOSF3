package classes;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import org.joda.time.DateTime;

@SuppressLint("DefaultLocale")
public class Semester extends BaseObject
{
	public final String University;
	public final String Faculty;
	public final String Year;
	public final String Group;
	public final String SemesterNumber;
	public final ArrayList<WeekCategory> WeekCategories;
	public final ArrayList<Week> Weeks;
	public final ArrayList<Discipline> Disciplines;
	public final ArrayList<Class> AllClasses;

	public Semester(String id, String university, String faculty, String year, String group, String semesterNumber, ArrayList<WeekCategory> weekCategories, ArrayList<Discipline> disciplines)
	{
		super(id);
		this.University = university;
		this.Faculty = faculty;
		this.Year = year;
		this.Group = group;
		this.SemesterNumber = semesterNumber;
		this.WeekCategories = weekCategories;
		this.Disciplines = disciplines;
		this.AllClasses = new ArrayList<Class>();
		this.Weeks = new ArrayList<Week>();
	}

	public void ProcessData()
	{
		for (Discipline discipline: this.Disciplines)
			for (Class cls: discipline.classes)
				this.AllClasses.add(cls);
		Utils.SortClassesChronologically(this.AllClasses);
		//
		for (Class cls: this.AllClasses)
		{
			Week week = new Week(Utils.GetMondayForDay(cls.when.date));
			if (Utils.IndexOfWeekInWeekList(week, this.Weeks) == -1)
				this.Weeks.add(week);
		}
		Utils.SortWeekList(this.Weeks);
	}

	public Week GetWeekByWeekFormat(String weekStr)
	{
		for (Week week: this.Weeks)
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 2; j++)
					if (weekStr.equals(week.FormatWeek(i == 1, j == 1)))
						return week;
		return null;
	}

	public ArrayList<Class> GetClassesByDate(DateTime date)
	{
		ArrayList<Class> result = new ArrayList<Class>();
		for (Class cls: this.AllClasses)
			if (cls.when.date.equals(date))
				result.add(cls);
		return result;
	}

	@Override
	public String toString()
	{
		return String.format("Semester [University=%s, Faculty=%s, Year=%s, Group=%s, SemesterNumber=%s, WeekCategories.size()=%d, Weeks.size()=%d, Disciplines.size()=%d, AllClasses.size()=%d]", University, Faculty, Year, Group,
				SemesterNumber, WeekCategories.size(), Weeks.size(), Disciplines.size(), AllClasses.size());
	}
}
