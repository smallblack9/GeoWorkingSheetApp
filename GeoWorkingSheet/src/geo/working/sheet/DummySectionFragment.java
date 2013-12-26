package geo.working.sheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
public class DummySectionFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public DummySectionFragment() {
	}

	private TextView dummyTextView;
	private ListView workingSheetList;
	private int sectionNumber;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main_dummy,
				container, false);

		sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
//		if (sectionNumber == 1) {
//			workingSheetList = (ListView) rootView
//					.findViewById(R.id.working_sheet_list);
//		} else {
//			dummyTextView = (TextView) rootView
//					.findViewById(R.id.section_label);
//		}
		new Thread(runnable).start();
		return rootView;
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("value");
			Log.i(DEBUG_TAG, "请求结果-->" + val);
			Gson gson = new Gson();
			Type listType = new TypeToken<List<WorkingSheet>>() {
			}.getType();
			List<WorkingSheet> workingSheets = gson.fromJson(val, listType);

			String ttt = "";
			for (WorkingSheet testJ : workingSheets) {
				strings.add(testJ.getName());
				ttt += "\nname: " + testJ.getName() + "\nEmail: "
						+ testJ.getEmail() + "\nKey: " + testJ.getKey()
						+ "\nQuestions: " + testJ.getQuestions().toString();
			}
			if (sectionNumber == 1) {
				workingSheetList = (ListView) rootView
						.findViewById(R.id.working_sheet_list);
				workingSheetList.setAdapter(new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_list_item_1,
						strings));
				workingSheetList.setTextFilterEnabled(true);
			} else {
				dummyTextView = (TextView) rootView
						.findViewById(R.id.section_label);
				dummyTextView.setText(ttt);
			}
		}
	};
	private String testUrl = "http://geo-working-sheet.appspot.com/WorkingSheetServlet";
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle b = new Bundle();

			String jsonString = getWorkingSheetsJsonStringFromURL(testUrl);
			b.putString("value", jsonString);
			msg.setData(b);
			handler.sendMessage(msg);

		}
	};

	private String getWorkingSheetsJsonStringFromURL(String urlString) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		URL url = null;
		try {
			url = new URL(urlString);

			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			Log.i(DEBUG_TAG, "urlString = " + urlString);
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

	private ArrayList<String> strings = new ArrayList<String>();
	private static final String DEBUG_TAG = "DummySectionFragment";

}
