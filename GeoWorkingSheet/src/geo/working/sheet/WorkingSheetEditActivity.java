package geo.working.sheet;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class WorkingSheetEditActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String DEBUG_TAG = "WorkingSheetEditActivity";
	private ListView workingSheet;
	private static final int SELECT_PICTURE = 1;
	private ImageView img;
	private String selectedImagePath;
	private int selectedItemId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_working_sheet_edit);
		workingSheet = (ListView) findViewById(R.id.working_sheet);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] { "Save", "Export",
								"Send"
						/*
						 * getString(R.string.title_section1),
						 * getString(R.string.title_section2),
						 * getString(R.string.title_section3),
						 */}), this);
		Intent intent = getIntent();
		String[] questionStrings = intent
				.getStringArrayExtra(EntryActivity.EXTRA_MESSAGE);
		ArrayList<Question> questions = new ArrayList<Question>();
		for (int i = 0; i < questionStrings.length; i++) {
			Question q = new Question();
			q.setQuestion(questionStrings[i]);
			questions.add(q);
		}
		QuestionAdapter mAdapter = new QuestionAdapter(this, questions);
		workingSheet.setAdapter(mAdapter);
	}

	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.working_sheet_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		// Fragment fragment = new DummySectionFragment();
		// Bundle args = new Bundle();
		// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		// fragment.setArguments(args);
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.container, fragment).commit();
		return true;
	}

	// /**
	// * A dummy fragment representing a section of the app, but that simply
	// * displays dummy text.
	// */
	// public static class DummySectionFragment extends Fragment {
	// /**
	// * The fragment argument representing the section number for this
	// * fragment.
	// */
	// public static final String ARG_SECTION_NUMBER = "section_number";
	//
	// public DummySectionFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(
	// R.layout.fragment_working_sheet_edit_dummy, container,
	// false);
	// TextView dummyTextView = (TextView) rootView
	// .findViewById(R.id.section_label);
	// dummyTextView.setText(Integer.toString(getArguments().getInt(
	// ARG_SECTION_NUMBER)));
	// return rootView;
	// }
	// }

	public class Question {
		private String textAnswer = "";
		private String question = "";
		private Drawable imageAnswer = null;

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public String getTextAnswer() {
			return textAnswer;
		}

		public void setTextAnswer(String textAnswer) {
			this.textAnswer = textAnswer;
		}

		public Drawable getImageAnswer() {
			return imageAnswer;
		}

		public void setImageAnswer(Drawable imageAnswer) {
			this.imageAnswer = imageAnswer;
		}
	}

	public static class ViewHolder {

		public TextView textQuestionTitle;
		public TextView textQuestion;
		public TextView textAnswerTitle;
		public EditText textAnswer;
		public TextView textImageAnswerTitle;
		public Button btnPickImage;
		public ImageView imageAnswer;
		public LinearLayout linearLayout;

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				Log.i(DEBUG_TAG, "pos=" + selectedItemId);
				Log.i(DEBUG_TAG, "workingSheet=" + workingSheet);
				Log.i(DEBUG_TAG, " workingSheet.getFirstVisiblePosition()="
						+ workingSheet.getFirstVisiblePosition());
				Log.i(DEBUG_TAG, "workingSheet.getLastVisiblePosition()="
						+ workingSheet.getLastVisiblePosition());
				Log.i(DEBUG_TAG, "workingSheet.getChildAt(selectedItemId)="
						+ workingSheet.getChildAt(selectedItemId));
				if (workingSheet.getLastVisiblePosition() < selectedItemId
						|| workingSheet.getFirstVisiblePosition() > selectedItemId) {

				}
				workingSheet.setSelection(selectedItemId);
				img = (ImageView) workingSheet.getChildAt(selectedItemId)
						.findViewById(R.id.image_answer);
				img.setImageURI(selectedImageUri);
			}
		}
	}

	public class QuestionAdapter extends BaseAdapter {
		private ArrayList<Question> questions = null;
		private final Context context;
		private int count = 0;

		public QuestionAdapter(Context context, ArrayList<Question> questions) {
			super();
			this.context = context;
			this.questions = questions;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				LayoutInflater inflator = ((Activity) context)
						.getLayoutInflater();
				convertView = inflator.inflate(
						R.layout.list_item_working_sheet, null);
				viewHolder = new ViewHolder();

				viewHolder.linearLayout = (LinearLayout) convertView
						.findViewById(R.id.list_item_layout);
				convertView.setTag(viewHolder);

				Log.i(DEBUG_TAG, "position = " + position);
				viewHolder.textQuestionTitle = (TextView) convertView
						.findViewById(R.id.question_title);
				viewHolder.textQuestion = (TextView) convertView
						.findViewById(R.id.question);
				viewHolder.textAnswerTitle = (TextView) convertView
						.findViewById(R.id.text_answer_title);
				viewHolder.textAnswer = (EditText) convertView
						.findViewById(R.id.text_answer);
				viewHolder.textImageAnswerTitle = (TextView) convertView
						.findViewById(R.id.image_answer_title);
				viewHolder.imageAnswer = (ImageView) convertView
						.findViewById(R.id.image_answer);
				viewHolder.btnPickImage = (Button) convertView
						.findViewById(R.id.button_pick_image);

			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if (questions.size() > 0) {
				count++;
				Log.i(DEBUG_TAG, "count = " + count + " position = " + position);

				viewHolder.textAnswerTitle.setText("Answer");
				viewHolder.textImageAnswerTitle.setText("Picture");
				viewHolder.textQuestionTitle.setText("Question "
						+ (position + 1));
				viewHolder.textQuestion.setText(questions.get(position)
						.getQuestion());
				viewHolder.btnPickImage
						.setOnClickListener(new ImageButtonOnClickListener(
								position));
				viewHolder.textAnswer.setText(questions.get(position)
						.getTextAnswer());
				viewHolder.textAnswer
						.addTextChangedListener(new EditTextWatcher(position));
			}

			return convertView;
		}

		@Override
		public int getCount() {
			return questions.size();
		}

		@Override
		public Object getItem(int arg0) {
			return questions.get(arg0).getTextAnswer();
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		private class EditTextWatcher implements TextWatcher {
			private int itemId;

			public EditTextWatcher(int id) {
				itemId = id;
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				questions.get(itemId).setTextAnswer(s.toString());
				// Toast.makeText(mContext, "Q：" + listPosititon +
				// " Ans："+ questions.get(listPosititon).getTextAnswer(),
				// Toast.LENGTH_LONG).show();
				Log.i(DEBUG_TAG, "Q：" + itemId + " Ans："
						+ questions.get(itemId).getTextAnswer());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		}

		private class ImageButtonOnClickListener implements OnClickListener {
			private int itemId;

			public ImageButtonOnClickListener(int id) {
				itemId = id;
			}

			@Override
			public void onClick(View v) {
				selectedItemId = itemId;

				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						SELECT_PICTURE);

			}

		}

	}

}
