package bba.androidosf3;

import classes.AppSettings;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;

public class OptionsActivity extends Activity
{
	public static AppSettings ApplicationSettings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		CheckBox checkBox = (CheckBox) findViewById(R.id.option1_checkbox);
		checkBox.setChecked(OptionsActivity.ApplicationSettings.EnlargeUserInterface);
		checkBox = (CheckBox) findViewById(R.id.option2_checkbox);
		checkBox.setChecked(OptionsActivity.ApplicationSettings.RememberLastSemester);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}
}
