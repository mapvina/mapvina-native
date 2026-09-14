package io.github.mapvina.android.testapp.maps.widgets;

import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import io.github.mapvina.android.maps.MapVinaMap;
import io.github.mapvina.android.testapp.activity.EspressoTest;

import org.hamcrest.Matcher;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

public class LogoTest extends EspressoTest {

  @Test
  public void testDefault() {
    validateTestSetup();
    onView(withTagValue(is("logoView"))).check(matches(isDisplayed()));
  }

  @Test
  public void testUsesHorizontalBrandLogo() {
    validateTestSetup();
    onView(withTagValue(is("logoView"))).check((view, noViewFoundException) ->
            assertTrue("MapVina logo should be wider than it is tall", view.getWidth() > view.getHeight()));
  }

  @Test
  public void testDisabled() {
    validateTestSetup();

    onView(withTagValue(is("logoView")))
            .perform(new DisableAction(mapvinaMap))
            .check(matches(not(isDisplayed())));
  }

  private class DisableAction implements ViewAction {

    private MapVinaMap mapvinaMap;

    DisableAction(MapVinaMap map) {
      mapvinaMap = map;
    }

    @Override
    public Matcher<View> getConstraints() {
      return isDisplayed();
    }

    @Override
    public String getDescription() {
      return getClass().getSimpleName();
    }

    @Override
    public void perform(UiController uiController, View view) {
      mapvinaMap.getUiSettings().setLogoEnabled(false);
    }
  }
}
