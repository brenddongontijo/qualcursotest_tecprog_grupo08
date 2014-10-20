package unb.mdsgpp.qualcurso.test.instrumentation;

import unb.mdsgpp.qualcurso.CourseListFragment;
import unb.mdsgpp.qualcurso.InstitutionListFragment;
import unb.mdsgpp.qualcurso.MainActivity;
import unb.mdsgpp.qualcurso.R;
import unb.mdsgpp.qualcurso.SearchByIndicatorFragment;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.Surface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class TestSearchByIndicator extends ActivityInstrumentationTestCase2<MainActivity> {
	private MainActivity mActivity;
	private Instrumentation mInstrumentation;

	public TestSearchByIndicator() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);
		this.mActivity = getActivity();
		this.mInstrumentation = getInstrumentation();
	}
	
	public void openDrawerOptionAt(int position){
		Fragment nd = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		DrawerLayout mDrawerLayout = (DrawerLayout) mActivity.findViewById(R.id.drawer_layout);
		
		if(!mDrawerLayout.isDrawerOpen(GravityCompat.START)){
			View v = nd.getView().focusSearch(View.FOCUS_FORWARD);
			TouchUtils.clickView(this, v);
		}
		ListView nl = (ListView)nd.getView().findViewById(R.id.navigation_list_view);
		TouchUtils.clickView(this, nl.getChildAt(position));
	}

	public void testShoudOpenSearchByIndicatorFragment() {
		openDrawerOptionAt(1);
		Fragment search = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		assertTrue(search instanceof SearchByIndicatorFragment);
	}

	public void testShoudSearchByIndicatorFragmentDisableUpperlimitWhenMaxCheckboxIsEnable() {
		openDrawerOptionAt(1);
		Fragment search = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		View maximumCheckbox = search.getView().findViewById(R.id.maximum);
		EditText secondNumber = (EditText) search.getView().findViewById(R.id.secondNumber);

		TouchUtils.clickView(this, maximumCheckbox);

		assertFalse(secondNumber.isEnabled());
	}

	public void testShoudSearchByIndicatorFragmentSetZeroToFirstNumberAndCheckMaximumIfTheUserJustPressSearch() {
		openDrawerOptionAt(1);
		Fragment search = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		View buttonSearch = search.getView().findViewById(R.id.buttonSearch);

		CheckBox maximumCheckbox = (CheckBox) search.getView().findViewById(R.id.maximum);
		EditText firstNumber = (EditText) search.getView().findViewById(R.id.firstNumber);

		TouchUtils.clickView(this, buttonSearch);

		assertEquals("0", firstNumber.getText().toString());
		assertTrue(maximumCheckbox.isChecked());
	}

	public void testShoudSearchByIndicatorFragmentSetListToInstitutionAndYearTo2010IfOnlyTheIndicatorIsSet() {
		openDrawerOptionAt(1);
		Fragment search = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		View buttonSearch = search.getView().findViewById(R.id.buttonSearch);

		Spinner listSelectionSpinner = (Spinner) search.getView().findViewById(R.id.course_institution);
		Spinner yearSpinner = (Spinner) search.getView().findViewById(R.id.year);
		final Spinner filterFieldSpinner = (Spinner) search.getView().findViewById(R.id.field);
		
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				filterFieldSpinner.setSelection(4);
			}
		});
		mInstrumentation.waitForIdleSync();

		TouchUtils.clickView(this, buttonSearch);
		
		assertEquals(listSelectionSpinner.getAdapter().getCount()-1 , listSelectionSpinner.getSelectedItemPosition());
		assertEquals(yearSpinner.getAdapter().getCount()-1, yearSpinner.getSelectedItemPosition());
	}

	public void testShoudSearchByIndicatorFragmentListCoursesWithInstitutionsByItsFilters() {
		openDrawerOptionAt(1);
		Fragment search = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		final Spinner listSelectionSpinner = (Spinner) search.getView().findViewById(R.id.course_institution);
		final Spinner yearSpinner = (Spinner) search.getView().findViewById(R.id.year);
		final Spinner filterFieldSpinner = (Spinner) search.getView().findViewById(R.id.field);
		final EditText firstNumber = (EditText) search.getView().findViewById(R.id.firstNumber);
		final EditText secondNumber = (EditText) search.getView().findViewById(R.id.secondNumber);
		View buttonSearch = search.getView().findViewById(R.id.buttonSearch);
		

		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listSelectionSpinner.requestFocus();
				listSelectionSpinner.setSelection(1); // Course

				yearSpinner.requestFocus();
				yearSpinner.setSelection(2); // 2010

				filterFieldSpinner.requestFocus();
				filterFieldSpinner.setSelection(1); // triennial evaluation

				firstNumber.requestFocus();
				firstNumber.setText("5");

				secondNumber.requestFocus();
				secondNumber.setText("7");
			}
		});

		TouchUtils.clickView(this, buttonSearch);
		Fragment searchList = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.search_list);
		ListView resultList = (ListView) searchList.getView().findViewById(android.R.id.list);
		TouchUtils.clickView(this, resultList.getChildAt(0));

		this.mActivity = getActivity();
		searchList = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		resultList = (ListView) searchList.getView().findViewById(android.R.id.list);

		assertTrue(searchList instanceof InstitutionListFragment);
	}

	public void testShoudSearchByIndicatorFragmentListCoursesWithCoursesByItsFilters() {
		openDrawerOptionAt(1);
		Fragment search = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		final Spinner listSelectionSpinner = (Spinner) search.getView().findViewById(R.id.course_institution);
		final Spinner yearSpinner = (Spinner) search.getView().findViewById(R.id.year);
		final Spinner filterFieldSpinner = (Spinner) search.getView().findViewById(R.id.field);
		final EditText firstNumber = (EditText) search.getView().findViewById(R.id.firstNumber);
		final EditText secondNumber = (EditText) search.getView().findViewById(R.id.secondNumber);
		View buttonSearch = search.getView().findViewById(R.id.buttonSearch);

		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				listSelectionSpinner.requestFocus();
				listSelectionSpinner.setSelection(2); // Institution

				yearSpinner.requestFocus();
				yearSpinner.setSelection(2); // 2010

				filterFieldSpinner.requestFocus();
				filterFieldSpinner.setSelection(1); // triennial evaluation

				firstNumber.requestFocus();
				firstNumber.setText("3");

				secondNumber.requestFocus();
				secondNumber.setText("4");
			}
		});

		TouchUtils.clickView(this, buttonSearch);
		Fragment searchList = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.search_list);
		ListView resultList = (ListView) searchList.getView().findViewById(android.R.id.list);
		TouchUtils.clickView(this, resultList.getChildAt(0));

		this.mActivity = getActivity();
		searchList = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);

		resultList = (ListView) searchList.getView().findViewById(android.R.id.list);

		assertTrue(searchList instanceof CourseListFragment);
	}
	
	public void testShouldTestOnSavedInstanceState() throws InterruptedException{
		openDrawerOptionAt(1);
		Fragment fragment = this.mActivity.getSupportFragmentManager().findFragmentById(R.id.container);
		if(mActivity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mInstrumentation.waitForIdleSync();
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mInstrumentation.waitForIdleSync();
		} else {
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			mInstrumentation.waitForIdleSync();
			mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mInstrumentation.waitForIdleSync();
		}
		while(mActivity.getWindowManager().getDefaultDisplay().getRotation() != Surface.ROTATION_0){
			Thread.sleep(500);
		}
		mInstrumentation.waitForIdleSync();
		assertTrue(fragment instanceof SearchByIndicatorFragment);
	}
}
