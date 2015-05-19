package classes;

import android.annotation.SuppressLint;
import java.util.ArrayList;

@SuppressLint("DefaultLocale")
public class Discipline extends NamedObject
{
	public final int color1;
	public final int color2;
	public final ArrayList<Class> classes;

	public Discipline(String id, String name, int color1, int color2, ArrayList<Class> classes)
	{
		super(id, name);
		this.color1 = color1;
		this.color2 = color2;
		this.classes = classes;
	}

	@Override
	public String toString()
	{
		return String.format("Discipline [%s, color1=%s, color2=%s, classes.size()=%d]", super.toString(), color1, color2, classes.size());
	}
}
