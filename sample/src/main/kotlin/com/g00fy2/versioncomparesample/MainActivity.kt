package com.g00fy2.versioncomparesample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.g00fy2.versioncompare.Version
import com.g00fy2.versioncomparesample.databinding.ActivityMainBinding

class MainActivity : Activity() {

  private lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    binding.compareButtonTextview.setOnClickListener { compareVersions() }
    binding.versionBEdittext.setOnEditorActionListener { _, _, _ ->
      compareVersions()
      true
    }
  }

  private fun compareVersions() {
    val versionStringA = binding.versionAEdittext.text.toString()
    val versionStringB = binding.versionBEdittext.text.toString()

    if (versionStringA.isNotBlank() && versionStringB.isNotBlank()) {
      val versionA = Version(versionStringA)
      val versionB = Version(versionStringB)

      binding.isHigherCheckedtextview.isChecked = versionA.isHigherThan(versionB)
      binding.isLowerCheckedtextview.isChecked = versionA.isLowerThan(versionB)
      binding.isEqualCheckedtextview.isChecked = versionA.isEqual(versionB)

      setVersionDescriptionViews(versionA, versionB)
    } else {
      binding.isHigherCheckedtextview.isChecked = false
      binding.isLowerCheckedtextview.isChecked = false
      binding.isEqualCheckedtextview.isChecked = false
      binding.versionDescriptionLinearlayout.visibility = View.GONE
    }

    hideKeyboard()
  }

  private fun setVersionDescriptionViews(versionA: Version, versionB: Version) {
    binding.subversionsATextview.text = versionA.subversionNumbers.joinToString(separator = ".").ifEmpty { "invalid" }
    binding.majorATextview.text = versionA.major.toString()
    binding.minorATextview.text = versionA.minor.toString()
    binding.patchATextview.text = versionA.patch.toString()
    binding.suffixATextview.text = versionA.suffix

    binding.subversionsBTextview.text = versionB.subversionNumbers.joinToString(separator = ".").ifEmpty { "invalid" }
    binding.majorBTextview.text = versionB.major.toString()
    binding.minorBTextview.text = versionB.minor.toString()
    binding.patchBTextview.text = versionB.patch.toString()
    binding.suffixBTextview.text = versionB.suffix

    binding.versionDescriptionLinearlayout.visibility = View.VISIBLE
  }

  private fun hideKeyboard() =
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
