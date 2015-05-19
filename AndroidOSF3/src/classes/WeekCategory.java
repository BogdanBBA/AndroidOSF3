package classes;

import android.annotation.SuppressLint;
import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class WeekCategory extends NamedObject
{
	public final ArrayList<Week> weeks;

	public WeekCategory(String id, String name, ArrayList<Week> weeks)
	{
		super(id, name);
		this.weeks = weeks;
	}

	@Override
	public String toString()
	{
		return String.format("WeekCategory [%s, weeks.size()=%d]", super.toString(), weeks.size());
	}
}
