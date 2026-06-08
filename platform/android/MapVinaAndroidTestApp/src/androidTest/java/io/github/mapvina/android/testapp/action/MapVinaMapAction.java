package io.github.mapvina.android.testapp.action;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import io.github.mapvina.android.maps.MapVinaMap;

import org.hamcrest.Matcher;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MapVinaMapAction implements ViewAction {

  private OnInvokeActionListener invokeViewAction;
  private MapVinaMap mapvinaMap;

  public MapVinaMapAction(OnInvokeActionListener invokeViewAction, MapVinaMap mapvinaMap) {
    this.invokeViewAction = invokeViewAction;
    this.mapvinaMap = mapvinaMap;
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
    invokeViewAction.onInvokeAction(uiController, mapvinaMap);
  }

  public static void invoke(MapVinaMap mapvinaMap, OnInvokeActionListener invokeViewAction) {
    onView(withId(android.R.id.content)).perform(new MapVinaMapAction(invokeViewAction, mapvinaMap));
  }

  public interface OnInvokeActionListener {
    void onInvokeAction(@NonNull UiController uiController, @NonNull MapVinaMap mapvinaMap);
  }
}
