/*
 * Copyright (c) 2021. Pisey Sen.
 * @Created by piseysen(IT Application) on 05/02/2021 5:36 PM
 */

package com.vb.helperlibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.autofill.AutofillManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * com.facebook.internal is solely for the use of other packages within the Facebook SDK for
 * Android. Use of any of the classes in this package is unsupported, and they may be modified or
 * removed without warning at any time.
 */
public final class Utility {
  static final String LOG_TAG = "FacebookSDK";
  private static final String HASH_ALGORITHM_MD5 = "MD5";
  private static final String HASH_ALGORITHM_SHA1 = "SHA-1";
  private static final String HASH_ALGORITHM_SHA256 = "SHA-256";
  private static final String URL_SCHEME = "https";
  private static final String EXTRA_APP_EVENTS_INFO_FORMAT_VERSION = "a2";

  private static final String UTF8 = "UTF-8";

  // This is the default used by the buffer streams, but they trace a warning if you do not
  // specify.
  public static final int DEFAULT_STREAM_BUFFER_SIZE = 8192;

  // Refresh extended device info every 30 minutes
  private static final int REFRESH_TIME_FOR_EXTENDED_DEVICE_INFO_MILLIS = 30 * 60 * 1000;

  private static final String noCarrierConstant = "NoCarrier";

  private static int numCPUCores = 0;

  private static long timestampOfLastCheck = -1;
  private static long totalExternalStorageGB = -1;
  private static long availableExternalStorageGB = -1;
  private static String deviceTimezoneAbbreviation = "";
  private static String deviceTimeZoneName = "";
  private static String carrierName = noCarrierConstant;

  // https://stackoverflow.com/questions/39784415/how-to-detect-programmatically-if-android-app-is-running-in-chrome-book-or-in
  private static final String ARC_DEVICE_PATTERN = ".+_cheets|cheets_.+";

  /**
   * Each array represents a set of closed or open Range, like so: [0,10,50,60] - Ranges are {0-9},
   * {50-59} [20] - Ranges are {20-} [30,40,100] - Ranges are {30-39}, {100-}
   *
   * <p>All Ranges in the array have a closed lower bound. Only the last Range in each array may be
   * open. It is assumed that the passed in arrays are sorted with ascending order. It is assumed
   * that no two elements in a given are equal (i.e. no 0-length ranges)
   *
   * <p>The method returns an intersect of the two passed in Range-sets
   *
   * @param range1 The first range
   * @param range2 The second range
   * @return The intersection of the two ranges.
   */
  public static int[] intersectRanges(int[] range1, int[] range2) {
    if (range1 == null) {
      return range2;
    } else if (range2 == null) {
      return range1;
    }

    int[] outputRange = new int[range1.length + range2.length];
    int outputIndex = 0;
    int index1 = 0, lower1, upper1;
    int index2 = 0, lower2, upper2;
    while (index1 < range1.length && index2 < range2.length) {
      int newRangeLower = Integer.MIN_VALUE, newRangeUpper = Integer.MAX_VALUE;
      lower1 = range1[index1];
      upper1 = Integer.MAX_VALUE;

      lower2 = range2[index2];
      upper2 = Integer.MAX_VALUE;

      if (index1 < range1.length - 1) {
        upper1 = range1[index1 + 1];
      }
      if (index2 < range2.length - 1) {
        upper2 = range2[index2 + 1];
      }

      if (lower1 < lower2) {
        if (upper1 > lower2) {
          newRangeLower = lower2;
          if (upper1 > upper2) {
            newRangeUpper = upper2;
            index2 += 2;
          } else {
            newRangeUpper = upper1;
            index1 += 2;
          }
        } else {
          index1 += 2;
        }
      } else {
        if (upper2 > lower1) {
          newRangeLower = lower1;
          if (upper2 > upper1) {
            newRangeUpper = upper1;
            index1 += 2;
          } else {
            newRangeUpper = upper2;
            index2 += 2;
          }
        } else {
          index2 += 2;
        }
      }

      if (newRangeLower != Integer.MIN_VALUE) {
        outputRange[outputIndex++] = newRangeLower;
        if (newRangeUpper != Integer.MAX_VALUE) {
          outputRange[outputIndex++] = newRangeUpper;
        } else {
          // If we reach an unbounded/open range, then we know we're done.
          break;
        }
      }
    }

    return Arrays.copyOf(outputRange, outputIndex);
  }

  // Returns true iff all items in subset are in superset, treating null and
  // empty collections as
  // the same.
  public static <T> boolean isSubset(Collection<T> subset, Collection<T> superset) {
    if ((superset == null) || (superset.size() == 0)) {
      return ((subset == null) || (subset.size() == 0));
    }

    HashSet<T> hash = new HashSet<T>(superset);
    for (T t : subset) {
      if (!hash.contains(t)) {
        return false;
      }
    }
    return true;
  }

  public static <T> boolean isNullOrEmpty(Collection<T> c) {
    return (c == null) || (c.size() == 0);
  }

  public static boolean isNullOrEmpty(String s) {
    return (s == null) || (s.length() == 0);
  }

  /**
   * Use this when you want to normalize empty and null strings This way, Utility.areObjectsEqual
   * can used for comparison, where a null string is to be treated the same as an empty string.
   *
   * @param s The string to coerce
   * @param valueIfNullOrEmpty The value if s is null or empty.
   * @return The original string s if it's not null or empty, otherwise the valueIfNullOrEmpty
   */
  public static String coerceValueIfNullOrEmpty(String s, String valueIfNullOrEmpty) {
    if (isNullOrEmpty(s)) {
      return valueIfNullOrEmpty;
    }

    return s;
  }

  public static <T> Collection<T> unmodifiableCollection(T... ts) {
    return Collections.unmodifiableCollection(Arrays.asList(ts));
  }

  public static <T> ArrayList<T> arrayList(T... ts) {
    ArrayList<T> arrayList = new ArrayList<T>(ts.length);
    for (T t : ts) {
      arrayList.add(t);
    }
    return arrayList;
  }

  public static <T> HashSet<T> hashSet(T... ts) {
    HashSet<T> hashSet = new HashSet<T>(ts.length);
    for (T t : ts) {
      hashSet.add(t);
    }
    return hashSet;
  }

  public static String md5hash(String key) {
    return hashWithAlgorithm(HASH_ALGORITHM_MD5, key);
  }

  public static String sha1hash(String key) {
    return hashWithAlgorithm(HASH_ALGORITHM_SHA1, key);
  }

  public static String sha1hash(byte[] bytes) {
    return hashWithAlgorithm(HASH_ALGORITHM_SHA1, bytes);
  }

  @Nullable
  public static String sha256hash(@Nullable String key) {
    if (key == null) {
      return null;
    }
    return hashWithAlgorithm(HASH_ALGORITHM_SHA256, key);
  }

  @Nullable
  public static String sha256hash(@Nullable byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    return hashWithAlgorithm(HASH_ALGORITHM_SHA256, bytes);
  }

  private static String hashWithAlgorithm(String algorithm, String key) {
    return hashWithAlgorithm(algorithm, key.getBytes());
  }

  private static String hashWithAlgorithm(String algorithm, byte[] bytes) {
    MessageDigest hash;
    try {
      hash = MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      return null;
    }
    return hashBytes(hash, bytes);
  }

  private static String hashBytes(MessageDigest hash, byte[] bytes) {
    hash.update(bytes);
    byte[] digest = hash.digest();
    StringBuilder builder = new StringBuilder();
    for (int b : digest) {
      builder.append(Integer.toHexString((b >> 4) & 0xf));
      builder.append(Integer.toHexString((b >> 0) & 0xf));
    }
    return builder.toString();
  }

  public static Uri buildUri(String authority, String path, Bundle parameters) {
    Uri.Builder builder = new Uri.Builder();
    builder.scheme(URL_SCHEME);
    builder.authority(authority);
    builder.path(path);
    if (parameters != null) {
      for (String key : parameters.keySet()) {
        Object parameter = parameters.get(key);
        if (parameter instanceof String) {
          builder.appendQueryParameter(key, (String) parameter);
        }
      }
    }
    return builder.build();
  }


  public static void putNonEmptyString(Bundle b, String key, String value) {
    if (!Utility.isNullOrEmpty(value)) {
      b.putString(key, value);
    }
  }

  public static void putCommaSeparatedStringList(Bundle b, String key, List<String> list) {
    if (list != null) {
      StringBuilder builder = new StringBuilder();
      for (String string : list) {
        builder.append(string);
        builder.append(",");
      }
      String commaSeparated = "";
      if (builder.length() > 0) {
        commaSeparated = builder.substring(0, builder.length() - 1);
      }
      b.putString(key, commaSeparated);
    }
  }

  public static void putUri(Bundle b, String key, Uri uri) {
    if (uri != null) {
      Utility.putNonEmptyString(b, key, uri.toString());
    }
  }

  public static boolean putJSONValueInBundle(Bundle bundle, String key, Object value) {
    if (value == null) {
      bundle.remove(key);
    } else if (value instanceof Boolean) {
      bundle.putBoolean(key, (boolean) value);
    } else if (value instanceof boolean[]) {
      bundle.putBooleanArray(key, (boolean[]) value);
    } else if (value instanceof Double) {
      bundle.putDouble(key, (double) value);
    } else if (value instanceof double[]) {
      bundle.putDoubleArray(key, (double[]) value);
    } else if (value instanceof Integer) {
      bundle.putInt(key, (int) value);
    } else if (value instanceof int[]) {
      bundle.putIntArray(key, (int[]) value);
    } else if (value instanceof Long) {
      bundle.putLong(key, (long) value);
    } else if (value instanceof long[]) {
      bundle.putLongArray(key, (long[]) value);
    } else if (value instanceof String) {
      bundle.putString(key, (String) value);
    } else if (value instanceof JSONArray) {
      bundle.putString(key, value.toString());
    } else if (value instanceof JSONObject) {
      bundle.putString(key, value.toString());
    } else {
      return false;
    }
    return true;
  }

  public static void closeQuietly(Closeable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (IOException ioe) {
      // ignore
    }
  }

  public static void disconnectQuietly(URLConnection connection) {
    if (connection != null && connection instanceof HttpURLConnection) {
      ((HttpURLConnection) connection).disconnect();
    }
  }



  static Map<String, Object> convertJSONObjectToHashMap(JSONObject jsonObject) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    JSONArray keys = jsonObject.names();
    for (int i = 0; i < keys.length(); ++i) {
      String key;
      try {
        key = keys.getString(i);
        Object value = jsonObject.get(key);
        if (value instanceof JSONObject) {
          value = convertJSONObjectToHashMap((JSONObject) value);
        }
        map.put(key, value);
      } catch (JSONException e) {
      }
    }
    return map;
  }

  public static Map<String, String> convertJSONObjectToStringMap(@NonNull JSONObject jsonObject) {
    HashMap<String, String> map = new HashMap<>();
    Iterator<String> keys = jsonObject.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      String value = jsonObject.optString(key);
      if (value != null) {
        map.put(key, value);
      }
    }
    return map;
  }

  public static List<String> convertJSONArrayToList(JSONArray jsonArray) {
    try {
      List<String> result = new ArrayList<>();
      for (int i = 0; i < jsonArray.length(); i++) {
        result.add(jsonArray.getString(i));
      }
      return result;
    } catch (JSONException je) {
      return new ArrayList<>();
    }
  }


  public static String readStreamToString(InputStream inputStream) throws IOException {
    BufferedInputStream bufferedInputStream = null;
    InputStreamReader reader = null;
    try {
      bufferedInputStream = new BufferedInputStream(inputStream);
      reader = new InputStreamReader(bufferedInputStream);
      StringBuilder stringBuilder = new StringBuilder();

      final int bufferSize = 1024 * 2;
      char[] buffer = new char[bufferSize];
      int n = 0;
      while ((n = reader.read(buffer)) != -1) {
        stringBuilder.append(buffer, 0, n);
      }

      return stringBuilder.toString();
    } finally {
      closeQuietly(bufferedInputStream);
      closeQuietly(reader);
    }
  }

  public static int copyAndCloseInputStream(InputStream inputStream, OutputStream outputStream)
      throws IOException {
    BufferedInputStream bufferedInputStream = null;
    int totalBytes = 0;
    try {
      bufferedInputStream = new BufferedInputStream(inputStream);

      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
        totalBytes += bytesRead;
      }
    } finally {
      if (bufferedInputStream != null) {
        bufferedInputStream.close();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return totalBytes;
  }

  public static boolean stringsEqualOrEmpty(String a, String b) {
    boolean aEmpty = TextUtils.isEmpty(a);
    boolean bEmpty = TextUtils.isEmpty(b);

    if (aEmpty && bEmpty) {
      // Both null or empty, they match.
      return true;
    }
    if (!aEmpty && !bEmpty) {
      // Both non-empty, check equality.
      return a.equals(b);
    }
    // One empty, one non-empty, can't match.
    return false;
  }



  public static <T> boolean areObjectsEqual(T a, T b) {
    if (a == null) {
      return b == null;
    }
    return a.equals(b);
  }

  public static boolean hasSameId(JSONObject a, JSONObject b) {
    if (a == null || b == null || !a.has("id") || !b.has("id")) {
      return false;
    }
    if (a.equals(b)) {
      return true;
    }
    String idA = a.optString("id");
    String idB = b.optString("id");
    if (idA == null || idB == null) {
      return false;
    }
    return idA.equals(idB);
  }

  public static String safeGetStringFromResponse(JSONObject response, String propertyName) {
    return response != null ? response.optString(propertyName, "") : "";
  }

  public static JSONObject tryGetJSONObjectFromResponse(JSONObject response, String propertyKey) {
    return response != null ? response.optJSONObject(propertyKey) : null;
  }

  public static JSONArray tryGetJSONArrayFromResponse(JSONObject response, String propertyKey) {
    return response != null ? response.optJSONArray(propertyKey) : null;
  }



  public static void deleteDirectory(File directoryOrFile) {
    if (!directoryOrFile.exists()) {
      return;
    }

    if (directoryOrFile.isDirectory()) {
      final File[] children = directoryOrFile.listFiles();
      if (children != null) {
        for (final File child : children) {
          deleteDirectory(child);
        }
      }
    }
    directoryOrFile.delete();
  }

  public static <T> List<T> asListNoNulls(T... array) {
    ArrayList<T> result = new ArrayList<T>();
    for (T t : array) {
      if (t != null) {
        result.add(t);
      }
    }
    return result;
  }

  public static List<String> jsonArrayToStringList(JSONArray jsonArray) throws JSONException {
    ArrayList<String> result = new ArrayList<>();

    for (int i = 0; i < jsonArray.length(); i++) {
      result.add(jsonArray.getString(i));
    }

    return result;
  }

  public static Set<String> jsonArrayToSet(JSONArray jsonArray) throws JSONException {
    Set<String> result = new HashSet<>();
    for (int i = 0; i < jsonArray.length(); i++) {
      result.add(jsonArray.getString(i));
    }

    return result;
  }

  public static String mapToJsonStr(Map<String, String> map) {
    if (map.isEmpty()) {
      return "";
    }
    try {
      JSONObject jsonObject = new JSONObject();
      for (Map.Entry<String, String> entry : map.entrySet()) {
        jsonObject.put(entry.getKey(), entry.getValue());
      }
      return jsonObject.toString();
    } catch (JSONException _e) {
      return "";
    }
  }

  public static Map<String, String> JsonStrToMap(String str) {
    if (str.isEmpty()) {
      return new HashMap<>();
    }
    try {
      Map<String, String> map = new HashMap<>();
      JSONObject jsonObject = new JSONObject(str);
      Iterator<String> keys = jsonObject.keys();
      while (keys.hasNext()) {
        String key = keys.next();
        map.put(key, jsonObject.getString(key));
      }
      return map;
    } catch (JSONException _e) {
      return new HashMap<>();
    }
  }


  public static void setAppEventExtendedDeviceInfoParameters(JSONObject params, Context appContext)
      throws JSONException {
    JSONArray extraInfoArray = new JSONArray();
    extraInfoArray.put(EXTRA_APP_EVENTS_INFO_FORMAT_VERSION);

    Utility.refreshPeriodicExtendedDeviceInfo(appContext);

    // Application Manifest info:
    String pkgName = appContext.getPackageName();
    int versionCode = -1;
    String versionName = "";

    try {
      PackageInfo pi = appContext.getPackageManager().getPackageInfo(pkgName, 0);
      versionCode = pi.versionCode;
      versionName = pi.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      // Swallow
    }

    // Application Manifest info:
    extraInfoArray.put(pkgName);
    extraInfoArray.put(versionCode);
    extraInfoArray.put(versionName);

    // OS/Device info
    extraInfoArray.put(Build.VERSION.RELEASE);
    extraInfoArray.put(Build.MODEL);

    // Locale
    Locale locale;
    try {
      locale = appContext.getResources().getConfiguration().locale;
    } catch (Exception e) {
      locale = Locale.getDefault();
    }
    extraInfoArray.put(locale.getLanguage() + "_" + locale.getCountry());

    // Time zone
    extraInfoArray.put(deviceTimezoneAbbreviation);

    // Carrier
    extraInfoArray.put(carrierName);

    // Screen dimensions
    int width = 0;
    int height = 0;
    double density = 0;
    try {
      WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
      if (wm != null) {
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        density = displayMetrics.density;
      }
    } catch (Exception e) {
      // Swallow
    }
    extraInfoArray.put(width);
    extraInfoArray.put(height);

    final DecimalFormat df = new DecimalFormat("#.##");
    extraInfoArray.put(df.format(density));

    // CPU Cores
    extraInfoArray.put(refreshBestGuessNumberOfCPUCores());

    // External Storage
    extraInfoArray.put(totalExternalStorageGB);
    extraInfoArray.put(availableExternalStorageGB);

    extraInfoArray.put(deviceTimeZoneName);

    params.put("extinfo", extraInfoArray.toString());
  }

  public static Method getMethodQuietly(
      Class<?> clazz, String methodName, Class<?>... parameterTypes) {
    try {
      return clazz.getMethod(methodName, parameterTypes);
    } catch (NoSuchMethodException ex) {
      return null;
    }
  }

  public static Method getMethodQuietly(
      String className, String methodName, Class<?>... parameterTypes) {
    try {
      Class<?> clazz = Class.forName(className);
      return getMethodQuietly(clazz, methodName, parameterTypes);
    } catch (ClassNotFoundException ex) {
      return null;
    }
  }

  public static Object invokeMethodQuietly(Object receiver, Method method, Object... args) {
    try {
      return method.invoke(receiver, args);
    } catch (IllegalAccessException ex) {
      return null;
    } catch (InvocationTargetException ex) {
      return null;
    }
  }

  /**
   * Returns the name of the current activity if the context is an activity, otherwise return
   * "unknown"
   */
  public static String getActivityName(Context context) {
    if (context == null) {
      return "null";
    } else if (context == context.getApplicationContext()) {
      return "unknown";
    } else {
      return context.getClass().getSimpleName();
    }
  }

  public interface Predicate<T> {
    public boolean apply(T item);
  }

  public static <T> List<T> filter(final List<T> target, final Predicate<T> predicate) {
    if (target == null) {
      return null;
    }
    final List<T> list = new ArrayList<T>();
    for (T item : target) {
      if (predicate.apply(item)) {
        list.add(item);
      }
    }
    return (list.size() == 0 ? null : list);
  }

  public interface Mapper<T, K> {
    public K apply(T item);
  }

  public static <T, K> List<K> map(final List<T> target, final Mapper<T, K> mapper) {
    if (target == null) {
      return null;
    }
    final List<K> list = new ArrayList<K>();
    for (T item : target) {
      final K mappedItem = mapper.apply(item);
      if (mappedItem != null) {
        list.add(mappedItem);
      }
    }
    return (list.size() == 0 ? null : list);
  }

  public static String getUriString(final Uri uri) {
    return (uri == null ? null : uri.toString());
  }

  public static boolean isWebUri(final Uri uri) {
    return (uri != null)
        && ("http".equalsIgnoreCase(uri.getScheme())
            || "https".equalsIgnoreCase(uri.getScheme())
            || "fbstaging".equalsIgnoreCase(uri.getScheme()));
  }

  public static boolean isContentUri(final Uri uri) {
    return (uri != null) && ("content".equalsIgnoreCase(uri.getScheme()));
  }

  public static boolean isFileUri(final Uri uri) {
    return (uri != null) && ("file".equalsIgnoreCase(uri.getScheme()));
  }



  public static Date getBundleLongAsDate(Bundle bundle, String key, Date dateBase) {
    if (bundle == null) {
      return null;
    }

    long secondsFromBase;

    Object secondsObject = bundle.get(key);
    if (secondsObject instanceof Long) {
      secondsFromBase = (Long) secondsObject;
    } else if (secondsObject instanceof String) {
      try {
        secondsFromBase = Long.parseLong((String) secondsObject);
      } catch (NumberFormatException e) {
        return null;
      }
    } else {
      return null;
    }

    if (secondsFromBase == 0) {
      return new Date(Long.MAX_VALUE);
    } else {
      return new Date(dateBase.getTime() + (secondsFromBase * 1000L));
    }
  }

  public static void writeStringMapToParcel(Parcel parcel, final Map<String, String> map) {
    if (map == null) {
      // 0 is for empty map, -1 to indicate null
      parcel.writeInt(-1);
    } else {
      parcel.writeInt(map.size());
      for (Map.Entry<String, String> entry : map.entrySet()) {
        parcel.writeString(entry.getKey());
        parcel.writeString(entry.getValue());
      }
    }
  }

  public static Map<String, String> readStringMapFromParcel(Parcel parcel) {
    int size = parcel.readInt();
    if (size < 0) {
      return null;
    }
    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < size; i++) {
      map.put(parcel.readString(), parcel.readString());
    }
    return map;
  }






  /**
   * Return our best guess at the available number of cores. Will always return at least 1.
   *
   * @return The minimum number of CPU cores
   */
  private static int refreshBestGuessNumberOfCPUCores() {
    // If we have calculated this before, return that value
    if (numCPUCores > 0) {
      return numCPUCores;
    }

    // Enumerate all available CPU files and try to count the number of CPU cores.
    try {
      File cpuDir = new File("/sys/devices/system/cpu/");
      File[] cpuFiles =
          cpuDir.listFiles(
              new FilenameFilter() {
                @Override
                public boolean accept(File dir, String fileName) {
                  return Pattern.matches("cpu[0-9]+", fileName);
                }
              });

      if (cpuFiles != null) {
        numCPUCores = cpuFiles.length;
      }
    } catch (Exception e) {
    }

    // If enumerating and counting the CPU cores fails, use the runtime. Fallback to 1 if
    // that returns bogus values.
    if (numCPUCores <= 0) {
      numCPUCores = Math.max(Runtime.getRuntime().availableProcessors(), 1);
    }
    return numCPUCores;
  }

  private static void refreshPeriodicExtendedDeviceInfo(Context appContext) {
//    if (timestampOfLastCheck == -1
//        || (System.currentTimeMillis() - timestampOfLastCheck)
//            >= com.vattanacbank.vbpaymentsdk.helpers.Utility.REFRESH_TIME_FOR_EXTENDED_DEVICE_INFO_MILLIS) {
//      timestampOfLastCheck = System.currentTimeMillis();
//      com.vattanacbank.vbpaymentsdk.helpers.Utility.refreshTimezone();
//      com.vattanacbank.vbpaymentsdk.helpers.Utility.refreshCarrierName(appContext);
//      com.vattanacbank.vbpaymentsdk.helpers.Utility.refreshTotalExternalStorage();
//      com.vattanacbank.vbpaymentsdk.helpers.Utility.refreshAvailableExternalStorage();
//    }
  }

  private static void refreshTimezone() {
    try {
      TimeZone tz = TimeZone.getDefault();
      deviceTimezoneAbbreviation = tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT);
      deviceTimeZoneName = tz.getID();
    } catch (AssertionError e) {
      // Workaround for a bug in Android that can cause crashes on Android 8.0 and 8.1
    } catch (Exception e) {
    }
  }

  /**
   * Get and cache the carrier name since this won't change during the lifetime of the app.
   *
   * @return The carrier name
   */
  private static void refreshCarrierName(Context appContext) {
    if (carrierName.equals(noCarrierConstant)) {
      try {
        TelephonyManager telephonyManager =
            ((TelephonyManager) appContext.getSystemService(Context.TELEPHONY_SERVICE));
        carrierName = telephonyManager.getNetworkOperatorName();
      } catch (Exception e) {
      }
    }
  }

  /** @return whether there is external storage: */
  private static boolean externalStorageExists() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  // getAvailableBlocks/getBlockSize deprecated but required pre-API v18
  @SuppressWarnings({"deprecation", "ExternalStorageUse"})
  private static void refreshAvailableExternalStorage() {
    try {
      if (externalStorageExists()) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        availableExternalStorageGB = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
      }
      availableExternalStorageGB = Utility.convertBytesToGB(availableExternalStorageGB);
    } catch (Exception e) {
      // Swallow
    }
  }

  // getAvailableBlocks/getBlockSize deprecated but required pre-API v18
  @SuppressWarnings({"deprecation", "ExternalStorageUse"})
  private static void refreshTotalExternalStorage() {
    try {
      if (externalStorageExists()) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        totalExternalStorageGB = (long) stat.getBlockCount() * (long) stat.getBlockSize();
      }
      totalExternalStorageGB = Utility.convertBytesToGB(totalExternalStorageGB);
    } catch (Exception e) {
      // Swallow
    }
  }

  private static long convertBytesToGB(double bytes) {
    return Math.round(bytes / (1024.0 * 1024.0 * 1024.0));
  }

  /**
   * Internal helper class that is used to hold three different permission lists (granted, declined
   * and expired)
   */
  public static class PermissionsLists {
    List<String> grantedPermissions;
    List<String> declinedPermissions;
    List<String> expiredPermissions;

    public PermissionsLists(
        List<String> grantedPermissions,
        List<String> declinedPermissions,
        List<String> expiredPermissions) {
      this.grantedPermissions = grantedPermissions;
      this.declinedPermissions = declinedPermissions;
      this.expiredPermissions = expiredPermissions;
    }

    public List<String> getGrantedPermissions() {
      return grantedPermissions;
    }

    public List<String> getDeclinedPermissions() {
      return declinedPermissions;
    }

    public List<String> getExpiredPermissions() {
      return expiredPermissions;
    }
  }

  public static PermissionsLists handlePermissionResponse(JSONObject result) throws JSONException {

    JSONObject permissions = result.getJSONObject("permissions");

    JSONArray data = permissions.getJSONArray("data");
    List<String> grantedPermissions = new ArrayList<>(data.length());
    List<String> declinedPermissions = new ArrayList<>(data.length());
    List<String> expiredPermissions = new ArrayList<>(data.length());

    for (int i = 0; i < data.length(); ++i) {
      JSONObject object = data.optJSONObject(i);
      String permission = object.optString("permission");
      if (permission == null || permission.equals("installed")) {
        continue;
      }
      String status = object.optString("status");
      if (status == null) {
        continue;
      }

      if (status.equals("granted")) {
        grantedPermissions.add(permission);
      } else if (status.equals("declined")) {
        declinedPermissions.add(permission);
      } else if (status.equals("expired")) {
        expiredPermissions.add(permission);
      }
    }

    return new PermissionsLists(grantedPermissions, declinedPermissions, expiredPermissions);
  }

  public static String generateRandomString(int length) {
    Random r = new Random();
    return new BigInteger(length * 5, r).toString(32);
  }

  /*
   * There is a bug on Android O that excludes the dialog's view hierarchy from the
   * ViewStructure used by Autofill because the window token is lost when the dialog
   * is resized, hence the token needs to be saved dialog is attached to a window and restored
   * when the dialog attributes change after it is resized.
   */
  public static boolean mustFixWindowParamsForAutofill(Context context) {
    // TODO: once this bug is fixed on Android P, checks for version here as well
    return isAutofillAvailable(context);
  }

  public static boolean isAutofillAvailable(Context context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
      // Autofill Framework is only available on Android O and higher
      return false;
    }
    AutofillManager afm = context.getSystemService(AutofillManager.class);
    // Returns whether autofill is supported by device or and enabled for current user.
    return afm != null && afm.isAutofillSupported() && afm.isEnabled();
  }

  /**
   * Determines whether the application is running on Chrome OS or not
   *
   * @param context the {@link Context}
   * @return true if the application is running on Chrome OS; false otherwise.
   */
  public static boolean isChromeOS(final Context context) {
    // TODO: (T29986208) android.os.Build.VERSION_CODES.O_MR1 and PackageManager.FEATURE_PC
    final boolean isChromeOS;
    if (Build.VERSION.SDK_INT >= 27) {
      isChromeOS = context.getPackageManager().hasSystemFeature("android.hardware.type.pc");
    } else {
      isChromeOS = Build.DEVICE != null && Build.DEVICE.matches(ARC_DEVICE_PATTERN);
    }
    return isChromeOS;
  }





}
