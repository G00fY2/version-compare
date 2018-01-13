package com.g00fy2.versioncomparesample;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.g00fy2.versioncompare.Version;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private EditText versionAEditText;
  private EditText versionBEditText;
  private LinearLayout versionDescriptionLinearLayout;
  private TextView subversionsATextView;
  private TextView majorATextView;
  private TextView minorATextView;
  private TextView patchATextView;
  private TextView suffixATextView;
  private TextView subversionsBTextView;
  private TextView majorBTextView;
  private TextView minorBTextView;
  private TextView patchBTextView;
  private TextView suffixBTextView;
  private CheckedTextView versionHigherThanCheckedTextView;
  private CheckedTextView versionLowerThanCheckedTextView;
  private CheckedTextView equalCheckedTextView;
  private Version versionA;
  private Version versionB;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  private void initVersions() {
    if (versionAEditText.getText().toString().trim().length() > 0
        && versionBEditText.getText().toString().trim().length() > 0) {

      String versA = versionAEditText.getText().toString();
      String versB = versionBEditText.getText().toString();

      versionA = new Version(versA);
      versionB = new Version(versB);

      versionDescriptionLinearLayout.setVisibility(View.VISIBLE);
    } else {
      versionA = null;
      versionB = null;
      versionDescriptionLinearLayout.setVisibility(View.GONE);
    }
  }

  private void compareVersions() {
    hideKeyboard();
    initVersions();
    setVersionTextViews();

    if (versionA != null && versionB != null) {
      versionHigherThanCheckedTextView.setChecked(versionA.isHigherThan(versionB));
      versionLowerThanCheckedTextView.setChecked(versionA.isLowerThan(versionB));
      equalCheckedTextView.setChecked(versionA.isEqual(versionB));
    } else {
      versionHigherThanCheckedTextView.setChecked(false);
      versionLowerThanCheckedTextView.setChecked(false);
      equalCheckedTextView.setChecked(false);
    }
  }

  private void initViews() {
    versionAEditText = findViewById(R.id.version_a_edittext);
    versionBEditText = findViewById(R.id.version_b_edittext);
    versionBEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          compareVersions();
          return true;
        }
        return false;
      }
    });
    versionDescriptionLinearLayout = findViewById(R.id.version_description_linearlayout);
    subversionsATextView = findViewById(R.id.subversions_a_textview);
    majorATextView = findViewById(R.id.major_a_textview);
    minorATextView = findViewById(R.id.minor_a_textview);
    patchATextView = findViewById(R.id.patch_a_textview);
    suffixATextView = findViewById(R.id.suffix_a_textview);
    subversionsBTextView = findViewById(R.id.subversions_b_textview);
    majorBTextView = findViewById(R.id.major_b_textview);
    minorBTextView = findViewById(R.id.minor_b_textview);
    patchBTextView = findViewById(R.id.patch_b_textview);
    suffixBTextView = findViewById(R.id.suffix_b_textview);
    versionHigherThanCheckedTextView = findViewById(R.id.is_higher_checkedtextview);
    versionLowerThanCheckedTextView = findViewById(R.id.is_lower_checkedtextview);
    equalCheckedTextView = findViewById(R.id.is_equal_checkedtextview);

    findViewById(R.id.compare_button_textview).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        compareVersions();
      }
    });
  }

  private void setVersionTextViews() {
    boolean versionsSet = versionA != null && versionB != null;
    subversionsATextView.setText(versionsSet ? listToString(versionA.getSubversionNumbers()) : "");
    majorATextView.setText(versionsSet ? String.valueOf(versionA.getMajor()) : "");
    minorATextView.setText(versionsSet ? String.valueOf(versionA.getMinor()) : "");
    patchATextView.setText(versionsSet ? String.valueOf(versionA.getPatch()) : "");
    suffixATextView.setText(versionsSet ? String.valueOf(versionA.getSuffix()) : "");

    subversionsBTextView.setText(versionsSet ? listToString(versionB.getSubversionNumbers()) : "");
    majorBTextView.setText(versionsSet ? String.valueOf(versionB.getMajor()) : "");
    majorBTextView.setText(versionsSet ? String.valueOf(versionB.getMajor()) : "");
    minorBTextView.setText(versionsSet ? String.valueOf(versionB.getMinor()) : "");
    patchBTextView.setText(versionsSet ? String.valueOf(versionB.getPatch()) : "");
    suffixBTextView.setText(versionsSet ? String.valueOf(versionB.getSuffix()) : "");
  }

  private String listToString(List<Integer> subversionNumbers) {
    if (subversionNumbers.size() == 0) {
      return "invalid";
    } else {
      StringBuilder sb = new StringBuilder();
      for (Integer integer : subversionNumbers) {
        if (sb.length() > 0) sb.append(".");
        sb.append(String.valueOf(integer));
      }
      return sb.toString();
    }
  }

  private void hideKeyboard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    View view = getCurrentFocus();
    if (imm != null && view != null) {
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}
