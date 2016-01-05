package org.droidfoot;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		if (Settings.scale) {
			CheckBoxPreference p = new CheckBoxPreference(this.getActivity());
			p.setKey("scale");
			p.setTitle(R.string.scaleTitle);
			p.setSummary(R.string.scaleSummary);
			p.setDefaultValue(Settings.scaleDefault);
			getPreferenceScreen().addPreference(p);
		}

		if (Settings.orientation) {
			ListPreference p = new ListPreference(this.getActivity());
			p.setKey("orientation");
			p.setTitle(R.string.orientationTitle);
			p.setSummary(R.string.orientationSummary);
			p.setEntries(R.array.orientationNames);
			p.setEntryValues(R.array.orientationValues);
			p.setDefaultValue(Settings.orientationDefault);
			getPreferenceScreen().addPreference(p);
//			p.setEnabled(false);
		}

		if (Settings.username) {
			EditTextPreference p = new EditTextPreference(this.getActivity());
			p.setKey("username");
			p.setTitle(R.string.usernameTitle);
			p.setSummary(R.string.usernameSummary);
			p.setDefaultValue(Settings.usernameDefault);
			getPreferenceScreen().addPreference(p);
		}

	}

}
