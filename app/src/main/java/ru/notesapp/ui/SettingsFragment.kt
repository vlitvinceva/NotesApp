package ru.notesapp.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.notesapp.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}