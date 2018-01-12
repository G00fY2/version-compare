package de.g00fy2.versioncompare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import de.g00fy2.github.vercomp.Version;

public class MainActivity extends AppCompatActivity {

  private EditText versionAEditText;
  private EditText versionBEditText;
  private CheckedTextView versionHigherThanCheckedTextView;
  private CheckedTextView versionLowerThanCheckedTextView;
  private CheckedTextView equalCheckedTextView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(de.g00fy2.versioncompare.R.layout.activity_main);

    versionAEditText = findViewById(de.g00fy2.versioncompare.R.id.version_a_edittext);
    versionBEditText = findViewById(de.g00fy2.versioncompare.R.id.version_b_edittext);
    versionHigherThanCheckedTextView = findViewById(de.g00fy2.versioncompare.R.id.is_higher_checkedtextview);
    versionLowerThanCheckedTextView = findViewById(de.g00fy2.versioncompare.R.id.is_lower_checkedtextview);
    equalCheckedTextView = findViewById(de.g00fy2.versioncompare.R.id.is_equal_checkedtextview);

    TextView compareButtonTextView = findViewById(R.id.compare_button_textview);
    compareButtonTextView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onCompareClicked();
      }
    });
  }

  private void onCompareClicked() {
    if (versionAEditText.getText().toString().trim().length() > 0
        && versionBEditText.getText().toString().trim().length() > 0) {
      Version versionA = new Version(versionAEditText.getText().toString());
      Version versionB = new Version(versionBEditText.getText().toString());

      versionHigherThanCheckedTextView.setChecked(versionA.isHigherThan(versionB));
      versionLowerThanCheckedTextView.setChecked(versionA.isLowerThan(versionB));
      equalCheckedTextView.setChecked(versionA.isEqual(versionB));
    } else {
      versionHigherThanCheckedTextView.setChecked(false);
      versionLowerThanCheckedTextView.setChecked(false);
      equalCheckedTextView.setChecked(false);
    }
  }
}
