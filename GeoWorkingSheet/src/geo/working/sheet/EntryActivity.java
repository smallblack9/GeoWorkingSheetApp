package geo.working.sheet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class EntryActivity extends Activity {

	private ListView workingSheetList;
	private static final String DEBUG_TAG = "EntryActivity";
	private String testUrl = "http://geo-working-sheet.appspot.com/WorkingSheetServlet";
	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	private ArrayList<WorkingSheet> workingSheets;
	private Context mContext = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_entry);
		workingSheetList = (ListView) findViewById(R.id.working_sheet_list);
		workingSheetList.setTextFilterEnabled(true);
		workingSheetList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
//				Toast.makeText(
//						mContext,
//						"ID：" + position + "   選單文字："
//								+ parent.getItemAtPosition(position).toString(),
//						Toast.LENGTH_LONG).show();
				goToEditWorkingSheet(position);
			}
		});
		mContext = this;
		new DownloadWorkingSheetTask().execute(testUrl);
	}

	/** Called when the user clicks the Send button */
	public void goToEditWorkingSheet(int position) {

		Intent intent = new Intent(this, WorkingSheetEditActivity.class);
		String[] strarray = new String[workingSheets.get(position)
				.getQuestions().size()];
		intent.putExtra(EXTRA_MESSAGE, workingSheets.get(position)
				.getQuestions().toArray(strarray));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String getWorkingSheetsJsonStringFromURL(String urlString) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			Log.i(DEBUG_TAG, "url.toString = " + url.toString());
			buffer = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private class DownloadWorkingSheetTask extends
			AsyncTask<String, Integer, String> {
		private ProgressDialog dialog;
		private ArrayList<String> workingSheetNames = new ArrayList<String>();

		public DownloadWorkingSheetTask() {
			dialog = new ProgressDialog(mContext);
		}

		protected void onPreExecute() {
			dialog.setTitle("Downloading...");
			dialog.setMessage("Please wait for a minite.");
			dialog.show();
		}

		// Do the long-running work in here
		protected String doInBackground(String... urlStrings) {
			int count = urlStrings.length;
			String jsonString = null;
			for (int i = 0; i < count; i++) {
				jsonString = getWorkingSheetsJsonStringFromURL(urlStrings[i]);
			}
			return jsonString;
		}

		// This is called each time you call publishProgress()
		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		// This is called when doInBackground() is finished
		protected void onPostExecute(String result) {
			// showNotification("Downloaded " + result + " bytes");
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<WorkingSheet>>() {
			}.getType();
			workingSheets = gson.fromJson(result, listType);

			String logString = "";
			for (WorkingSheet aWorkingSheet : workingSheets) {
				workingSheetNames.add(aWorkingSheet.getName());
				logString = "\nname: " + aWorkingSheet.getName() + "\nEmail: "
						+ aWorkingSheet.getEmail() + "\nKey: "
						+ aWorkingSheet.getKey() + "\nQuestions: "
						+ aWorkingSheet.getQuestions().toString();
				Log.i(DEBUG_TAG, "logString = " + logString);
			}
			workingSheetList.setAdapter(new ArrayAdapter<String>(mContext,
					android.R.layout.simple_list_item_1, workingSheetNames));
			dialog.dismiss();
		}
	}
}
