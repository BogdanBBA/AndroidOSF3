package bba.androidosf3;

import java.io.File;
import java.util.ArrayList;
import org.joda.time.DateTime;
import classes.AppSettings;
import classes.Class;
import classes.Database;
import classes.Paths;
import classes.Semester;
import classes.Utils;
import classes.Week;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity
{
	public static Database database;
	public ArrayList<ImageView> classIVs = new ArrayList<ImageView>();
	public AppSettings appSettings = new AppSettings();

	/**
	 * Called when the app is about to exit, or when an error occurs
	 * 
	 * @param title
	 *            the title of the message
	 * @param message
	 *            the message to display
	 * @param errorIcon
	 *            whether this is an error
	 */
	private void ShowMessageAndThreatenToExit(String title, String message, boolean errorIcon)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title).setMessage(message + "\n\nDo you want to close the application?");
		builder.setIcon(errorIcon ? android.R.drawable.ic_dialog_alert : android.R.drawable.ic_dialog_info);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(1);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Fill the given spinner with the given text strings
	 * 
	 * @param spinner
	 *            the spinner to fill
	 * @param text
	 *            the array of strings
	 */
	private void FillSpinnerWithData(Spinner spinner, ArrayList<String> text)
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, text);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}

	/**
	 * Called when the SEMESTER HAS BEEN CHANGED
	 * 
	 * @param textColor
	 *            the color to set the someone something
	 */
	private OnItemSelectedListener getSemesterSpinnerListener(final int textColor)
	{
		return new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				try
				{
					TextView tv = (TextView) parentView.getChildAt(0);
					tv.setTextSize(20);
					tv.setTypeface(null, Typeface.BOLD);
					tv.setTextColor(textColor);

					Spinner spinner = (Spinner) findViewById(R.id.semester_spinner);
					tv = (TextView) findViewById(R.id.title_label);
					Semester semester = MainActivity.database.GetSemesterByID(spinner.getSelectedItem().toString());
					tv.setText("Orar " + semester.Group + " / sem. " + semester.SemesterNumber);

					spinner = (Spinner) findViewById(R.id.week_spinner);
					ArrayList<String> spinnerText = new ArrayList<String>();
					for (Week week: semester.Weeks)
						spinnerText.add(week.FormatWeek(true, true));
					FillSpinnerWithData(spinner, spinnerText);
					spinner.setSelection(Utils.GetSemesterWeekMostRelevantToTodayPosition(semester), true);
				}
				catch (Exception e)
				{
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		};
	}

	/**
	 * Refreshes the class image list for the given class list
	 * 
	 * @param classes
	 *            the list of classes to display
	 */
	private void RefreshClassPictureBoxes(ArrayList<Class> classes)
	{
		final int idOffset = 666;
		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_relative_layout);
		for (int i = classes.size(); i < this.classIVs.size(); i++)
			this.classIVs.get(i).setVisibility(View.GONE);
		for (int i = 0; i < classes.size(); i++)
		{
			if (i >= this.classIVs.size())
			{
				ImageView classIV = new ImageView(this);
				classIV.setId(idOffset + i);
				classIV.setPadding(0, 12, 0, 0);
				this.classIVs.add(classIV);
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				params.addRule(RelativeLayout.BELOW, i == 0 ? R.id.dow_spinner : idOffset + i - 1);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				mainLayout.addView(classIV, params);
			}
			ImageView classPB = this.classIVs.get(i);
			classPB.setImageBitmap(ClassPictureBox.GeneratePixtureBoxForClass(classes.get(i), mainLayout.getWidth(), getResources().getColor(R.color.bg_color), getResources().getColor(R.color.time_color),
					getResources().getColor(R.color.discipline_color), getResources().getColor(R.color.prof_color), getResources().getColor(R.color.room_color)));
			classPB.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Called when the (WEEK or DAY OF WEEK) HAS BEEN CHANGED
	 * 
	 * @param textColor
	 *            the color to set the someone something
	 */
	private OnItemSelectedListener getWeekAndDowSpinnerListener(final int textColor)
	{
		return new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
			{
				try
				{
					TextView tv = (TextView) parentView.getChildAt(0);
					tv.setTextSize(20);
					tv.setTypeface(null, Typeface.BOLD);
					tv.setTextColor(textColor);

					String semStr = ((Spinner) findViewById(R.id.semester_spinner)).getSelectedItem().toString();
					String weekStr = ((Spinner) findViewById(R.id.week_spinner)).getSelectedItem().toString();
					String dowStr = ((Spinner) findViewById(R.id.dow_spinner)).getSelectedItem().toString();
					Semester semester = MainActivity.database.GetSemesterByID(semStr);
					Week week = semester.GetWeekByWeekFormat(weekStr);
					DateTime date = week.Monday.plusDays(Utils.StringIndexInStringArray(dowStr, Utils.DowNames_Romanian) - 1);
					ArrayList<Class> classes = semester.GetClassesByDate(date);
					RefreshClassPictureBoxes(classes);
				}
				catch (Exception e)
				{
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
			}
		};
	}

	/**
	 * The entry point into the app
	 * http://stackoverflow.com/questions/8515936/android
	 * -activity-life-cycle-what-are-all-these-methods-for
	 * http://developer.android
	 * .com/reference/android/app/Activity.html#ActivityLifecycle
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		File folder = new File(Paths.DatabaseFolder);
		if (!folder.exists())
			if (!folder.mkdir())
				ShowMessageAndThreatenToExit("Working folder ERROR", "Working folder \"" + folder.getAbsolutePath() + "\" does not exist and could not be created.", true);

		MainActivity.database = new Database();
		String loadResult = MainActivity.database.OpenDatabase();
		if (!loadResult.equals(""))
			ShowMessageAndThreatenToExit("Database open ERROR", loadResult, true);

	}

	@Override
	public void onStart()
	{
		super.onStart();

		setContentView(R.layout.activity_main);

		Spinner spinner = (Spinner) findViewById(R.id.semester_spinner);
		ArrayList<String> spinnerText = new ArrayList<String>();
		for (Semester semester: MainActivity.database.Semesters)
			spinnerText.add(semester.id);
		FillSpinnerWithData(spinner, spinnerText);
		spinner.setOnItemSelectedListener(this.getSemesterSpinnerListener(getResources().getColor(R.color.semester_spinner_text_color)));

		spinner = (Spinner) findViewById(R.id.week_spinner);
		spinner.setOnItemSelectedListener(this.getWeekAndDowSpinnerListener(getResources().getColor(R.color.week_spinner_text_color)));

		spinner = (Spinner) findViewById(R.id.dow_spinner);
		spinnerText = new ArrayList<String>();
		for (int i = 1; i <= 7; i++)
			spinnerText.add(Utils.DowNames_Romanian[i]);
		FillSpinnerWithData(spinner, spinnerText);
		spinner.setOnItemSelectedListener(this.getWeekAndDowSpinnerListener(getResources().getColor(R.color.dow_spinner_text_color)));
		spinner.setSelection(DateTime.now().getDayOfWeek() - 1);
	}

	/**
	 * 
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * 
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			OptionsActivity.ApplicationSettings = this.appSettings;
			setContentView(R.layout.activity_options);
			// ShowMessageAndThreatenToExit("Hehe :D",
			// "You're awesome for using this!\nI don't have any app settings to offer you though.",
			// false);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
