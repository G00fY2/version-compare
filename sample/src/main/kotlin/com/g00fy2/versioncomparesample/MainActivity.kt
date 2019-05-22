package com.g00fy2.versioncomparesample

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.g00fy2.versioncompare.Version
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    compare_button_textview.setOnClickListener { compareVersions() }
    version_b_edittext.setOnEditorActionListener { _, _, _ ->
      compareVersions()
      true
    }
  }

  private fun compareVersions() {
    val versionStringA = version_a_edittext.text.toString()
    val versionStringB = version_b_edittext.text.toString()

    if (versionStringA.isNotBlank() && versionStringB.isNotBlank()) {
      val versionA = Version(versionStringA)
      val versionB = Version(versionStringB)

      is_higher_checkedtextview.isChecked = versionA.isHigherThan(versionB)
      is_lower_checkedtextview.isChecked = versionA.isLowerThan(versionB)
      is_equal_checkedtextview.isChecked = versionA.isEqual(versionB)

      setVersionDescriptionViews(versionA, versionB)
    } else {
      is_higher_checkedtextview.isChecked = false
      is_lower_checkedtextview.isChecked = false
      is_equal_checkedtextview.isChecked = false
      version_description_linearlayout.visibility = View.GONE
    }

    hideKeyboard()
  }

  private fun setVersionDescriptionViews(versionA: Version, versionB: Version) {
    subversions_a_textview.text = versionA.subversionNumbers.joinToString(separator = ".").ifEmpty { "invalid" }
    major_a_textview.text = versionA.major.toString()
    minor_a_textview.text = versionA.minor.toString()
    patch_a_textview.text = versionA.patch.toString()
    suffix_a_textview.text = versionA.suffix

    subversions_b_textview.text = versionB.subversionNumbers.joinToString(separator = ".").ifEmpty { "invalid" }
    major_b_textview.text = versionB.major.toString()
    minor_b_textview.text = versionB.minor.toString()
    patch_b_textview.text = versionB.patch.toString()
    suffix_b_textview.text = versionB.suffix

    version_description_linearlayout.visibility = View.VISIBLE
  }

  private fun hideKeyboard() =
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
