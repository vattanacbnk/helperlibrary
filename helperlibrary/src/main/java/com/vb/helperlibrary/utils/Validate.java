/*
 * Copyright (c) 2021. Vattanac Bank.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.utils;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import java.util.Collection;
import java.util.List;


public final class Validate {

  private static final String TAG = Validate.class.getName();

  private static final String NO_INTERNET_PERMISSION_REASON =
      "No internet permissions granted for the app, please add "
          + "<uses-permission android:name=\"android.permission.INTERNET\" /> "
          + "to your AndroidManifest.xml.";

  private static final String FACEBOOK_ACTIVITY_NOT_FOUND_REASON =
      "FacebookActivity is not declared in the AndroidManifest.xml. If you are using the "
          + "facebook-common module or dependent modules please add "
          + "com.facebook.FacebookActivity to your AndroidManifest.xml file. See "
          + "https://developers.facebook.com/docs/android/getting-started for more info.";

  private static final String CONTENT_PROVIDER_NOT_FOUND_REASON =
      "A ContentProvider for this app was not set up in the AndroidManifest.xml, please "
          + "add %s as a provider to your AndroidManifest.xml file. See "
          + "https://developers.facebook.com/docs/sharing/android for more info.";

  private static final String CONTENT_PROVIDER_BASE = "com.facebook.app.FacebookContentProvider";

  public static final String CUSTOM_TAB_REDIRECT_URI_PREFIX = "fbconnect://cct.";

  public static void notNull(Object arg, String name) {
    if (arg == null) {
      throw new NullPointerException("Argument '" + name + "' cannot be null");
    }
  }

  public static <T> void notEmpty(Collection<T> container, String name) {
    if (container.isEmpty()) {
      throw new IllegalArgumentException("Container '" + name + "' cannot be empty");
    }
  }

  public static <T> void containsNoNulls(Collection<T> container, String name) {
    Validate.notNull(container, name);
    for (T item : container) {
      if (item == null) {
        throw new NullPointerException("Container '" + name + "' cannot contain null values");
      }
    }
  }

  public static void containsNoNullOrEmpty(Collection<String> container, String name) {
    Validate.notNull(container, name);
    for (String item : container) {
      if (item == null) {
        throw new NullPointerException("Container '" + name + "' cannot contain null values");
      }
      if (item.length() == 0) {
        throw new IllegalArgumentException("Container '" + name + "' cannot contain empty values");
      }
    }
  }

  public static <T> void notEmptyAndContainsNoNulls(Collection<T> container, String name) {
     Validate.containsNoNulls(container, name);
     Validate.notEmpty(container, name);
  }

//  public static void runningOnUiThread() {
//    if (!Looper.getMainLooper().equals(Looper.myLooper())) {
//      throw new FacebookException("This method should be called from the UI thread");
//    }
//  }

  public static void notNullOrEmpty(String arg, String name) {
    if (Utility.isNullOrEmpty(arg)) {
      throw new IllegalArgumentException("Argument '" + name + "' cannot be null or empty");
    }
  }

  public static void oneOf(Object arg, String name, Object... values) {
    for (Object value : values) {
      if (value != null) {
        if (value.equals(arg)) {
          return;
        }
      } else {
        if (arg == null) {
          return;
        }
      }
    }
    throw new IllegalArgumentException("Argument '" + name + "' was not one of the allowed values");
  }


  public static String hasAppID() {
    String id = "";
    if (id == null) {
      throw new IllegalStateException("No App ID found, please set the App ID.");
    }
    return id;
  }



  public static void hasInternetPermissions(Context context) {
    Validate.hasInternetPermissions(context, true);
  }

  public static void hasInternetPermissions(Context context, boolean shouldThrow) {
    Validate.notNull(context, "context");
    if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
        == PackageManager.PERMISSION_DENIED) {
      if (shouldThrow) {
        throw new IllegalStateException(NO_INTERNET_PERMISSION_REASON);
      } else {
        Log.w(TAG, NO_INTERNET_PERMISSION_REASON);
      }
    }
  }

  public static boolean hasWiFiPermission(Context context) {
    return hasPermission(context, Manifest.permission.ACCESS_WIFI_STATE);
  }

  public static boolean hasChangeWifiStatePermission(Context context) {
    return hasPermission(context, Manifest.permission.CHANGE_WIFI_STATE);
  }

  public static boolean hasLocationPermission(Context context) {
    return hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        || hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
  }

  public static boolean hasBluetoothPermission(Context context) {
    return hasPermission(context, Manifest.permission.BLUETOOTH)
        && hasPermission(context, Manifest.permission.BLUETOOTH_ADMIN);
  }

  public static boolean hasPermission(Context context, String permission) {
    return context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
  }

  public static void hasFacebookActivity(Context context) {
    Validate.hasFacebookActivity(context, true);
  }

  @SuppressWarnings("WrongConstant")
  public static void hasFacebookActivity(Context context, boolean shouldThrow) {
    Validate.notNull(context, "context");
    PackageManager pm = context.getPackageManager();
    ActivityInfo activityInfo = null;
    if (pm != null) {
      ComponentName componentName = new ComponentName(context, "com.facebook.FacebookActivity");
      try {
        activityInfo = pm.getActivityInfo(componentName, PackageManager.GET_ACTIVITIES);
      } catch (PackageManager.NameNotFoundException e) {
        // ignore
      }
    }
    if (activityInfo == null) {
      if (shouldThrow) {
        throw new IllegalStateException(FACEBOOK_ACTIVITY_NOT_FOUND_REASON);
      } else {
        Log.w(TAG, FACEBOOK_ACTIVITY_NOT_FOUND_REASON);
      }
    }
  }

  public static boolean hasCustomTabRedirectActivity(Context context, String redirectURI) {
    Validate.notNull(context, "context");
    PackageManager pm = context.getPackageManager();
    List<ResolveInfo> infos = null;
    if (pm != null) {
      Intent intent = new Intent();
      intent.setAction(Intent.ACTION_VIEW);
      intent.addCategory(Intent.CATEGORY_DEFAULT);
      intent.addCategory(Intent.CATEGORY_BROWSABLE);
      intent.setData(Uri.parse(redirectURI));

      infos = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
    }
    boolean hasActivity = false;

    if (infos != null) {
      for (ResolveInfo info : infos) {
        ActivityInfo activityInfo = info.activityInfo;
        if (activityInfo.name.equals("com.facebook.CustomTabActivity")
            && activityInfo.packageName.equals(context.getPackageName())) {
          hasActivity = true;
        } else {
          // another application is listening for this url scheme, don't open
          // Custom Tab for security reasons
          return false;
        }
      }
    }
    return hasActivity;
  }

  public static void hasContentProvider(Context context) {
    Validate.notNull(context, "context");
    String appId = Validate.hasAppID();
    PackageManager pm = context.getPackageManager();
    if (pm != null) {
      String providerName = CONTENT_PROVIDER_BASE + appId;
      if (pm.resolveContentProvider(providerName, 0) == null) {
        throw new IllegalStateException(
            String.format(CONTENT_PROVIDER_NOT_FOUND_REASON, providerName));
      }
    }
  }
}
