package io.github.mapvina.android.attribution;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class AttributionMeasureTest {

  @Test
  public void usesFullLogoWhenWideLayoutFits() {
    Bitmap fullLogo = Bitmap.createBitmap(60, 20, Bitmap.Config.ARGB_8888);
    Bitmap smallLogo = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);

    AttributionLayout layout = createMeasure(300, fullLogo, smallLogo).measure();

    assertSame(fullLogo, layout.getLogo());
    assertFalse(layout.isShortText());
  }

  @Test
  public void usesSmallLogoWithShortTextWhenOnlyCompactLayoutFits() {
    Bitmap fullLogo = Bitmap.createBitmap(60, 20, Bitmap.Config.ARGB_8888);
    Bitmap smallLogo = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);

    AttributionLayout layout = createMeasure(120, fullLogo, smallLogo).measure();

    assertSame(smallLogo, layout.getLogo());
    assertTrue(layout.isShortText());
  }

  private AttributionMeasure createMeasure(int snapshotWidth, Bitmap fullLogo, Bitmap smallLogo) {
    Bitmap snapshot = Bitmap.createBitmap(snapshotWidth, 100, Bitmap.Config.ARGB_8888);
    TextView longText = measuredTextView(100);
    TextView shortText = measuredTextView(75);

    return new AttributionMeasure.Builder()
      .setSnapshot(snapshot)
      .setLogo(fullLogo)
      .setLogoSmall(smallLogo)
      .setTextView(longText)
      .setTextViewShort(shortText)
      .setMarginPadding(5)
      .build();
  }

  private TextView measuredTextView(int width) {
    TextView textView = new TextView(RuntimeEnvironment.getApplication());
    textView.measure(
      View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(10, View.MeasureSpec.EXACTLY)
    );
    textView.layout(0, 0, width, 10);
    return textView;
  }
}
