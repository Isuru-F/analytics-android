package com.segment.android.integration;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.segment.android.Analytics;
import com.segment.android.Config;
import com.segment.android.errors.InvalidSettingsException;
import com.segment.android.integration.Integration;
import com.segment.android.integration.IntegrationState;
import com.segment.android.models.EasyJSONObject;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

/**
 * An Activity Unit Test that's capable of setting up a provider
 * for testing. Doesn't actually include tests, but expects
 * child to do so.
 *
 */
public abstract class BaseIntegrationInitializationActivity extends
		ActivityUnitTestCase<MockActivity> {

	protected MockActivity activity;
	protected Integration integration;

	public BaseIntegrationInitializationActivity() {
		super(MockActivity.class);
	}

	@BeforeClass
	protected void setUp() throws Exception {
		super.setUp();

		integration = getIntegration();

		Context context = getInstrumentation().getTargetContext();
		
		Intent intent = new Intent(context, MockActivity.class);
		startActivity(intent, null, null);
		
		activity = getActivity();
	}

	public abstract Integration getIntegration();

	public abstract EasyJSONObject getSettings();

	protected void reachInitializedState() {

		EasyJSONObject settings = getSettings();

		integration.reset();

		Assert.assertEquals(IntegrationState.NOT_INITIALIZED, integration.getState());

		try {
			integration.initialize(settings);

		} catch (InvalidSettingsException e) {
			Assert.assertTrue("Invalid settings.", false);
		}

		Assert.assertEquals(IntegrationState.INITIALIZED, integration.getState());
	}

	protected void reachEnabledState() {
		reachInitializedState();

		integration.enable();

		Assert.assertEquals(IntegrationState.ENABLED, integration.getState());
	}

	protected void reachReadyState() {
		reachEnabledState();

		// initialize since we can't get the proper context otherwise
		Analytics.initialize(activity, "testsecret", new Config());
		
		integration.onCreate(activity);

		integration.onActivityStart(activity);

		Assert.assertEquals(IntegrationState.READY, integration.getState());
	}

	@AfterClass
	protected void tearDown() throws Exception {
		super.tearDown();
		
		integration.flush();
	}
}
