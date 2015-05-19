package classes;

import android.os.Environment;

public abstract class Paths
{
	public static final String DatabaseFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/OSF3").getAbsolutePath();
	public static final String DatabaseFile = DatabaseFolder + "/database.xml";
	public static final String SettingsFile = DatabaseFolder + "/settings.xml";
	public static final String LogFile = DatabaseFolder + "/log.txt";
}
