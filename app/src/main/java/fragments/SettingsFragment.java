package fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.android.popularmoviesstage2.R;


// TODO : 119 ) Creating SettingsFragment for defining ListPreference
public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    // TODO : 120 ) Overriding onCreatePreferences -->
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

         /* Add 'general' preferences, defined in the XML file */
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();

        for (int i = 0; i < prefScreen.getPreferenceCount(); i++) {
            Preference pref = prefScreen.getPreference(i);
            if (!(pref instanceof CheckBoxPreference)) {
                String prefValue = sharedPreferences.getString(pref.getKey(), "");
                setPreferenceSummary(pref, prefValue);
            }
        }
    }

    // TODO : 121 ) Overriding setPreferenceSummary -->
    public void setPreferenceSummary(Preference preference, Object value) {
        String prefStringValue = value.toString();
        /* For list preferences, look up the correct display value in */
        /* the preference's 'entries' list (since they have separate labels/values). */
        if (preference instanceof ListPreference) {
            ListPreference listPref = (ListPreference) preference;
            int prefIndex = listPref.findIndexOfValue(prefStringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPref.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(prefStringValue);
        }
    }

    // TODO : 122 ) Overriding onStart -->
    @Override
    public void onStart() {
        super.onStart();
        /* Register the preference change listener */
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    // TODO : 123 ) Overriding onStop -->
    @Override
    public void onStop() {
        super.onStop();
        /* Unregister the preference change listener */
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    // TODO : 124 ) Overriding onSharedPreferenceChanged -->
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference != null) {
            if (!(preference instanceof CheckBoxPreference)) {
                String prefValue = sharedPreferences.getString(key, "");
                setPreferenceSummary(preference, prefValue);
            }
        }
    }
}
