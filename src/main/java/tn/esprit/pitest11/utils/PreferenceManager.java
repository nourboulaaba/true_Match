package tn.esprit.pitest11.utils;

import java.util.prefs.Preferences;

public class PreferenceManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(PreferenceManager.class);

    // Save data
    public static void save(String key, String value) {
        if (value!=null) prefs.put(key, value);
    }

    public static void save(String key, int value) {
        prefs.putInt(key, value);
    }

    public static void save(String key, boolean value) {
        prefs.putBoolean(key, value);
    }

    public static void save(String key, double value) {
        prefs.putDouble(key, value);
    }

    // Retrieve data
    public static String getString(String key, String defaultValue) {
        return prefs.get(key, defaultValue);
    }

    public static Double getDouble(String key, Double defaultValue) {
        return prefs.getDouble(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    // Remove data
    public static void remove(String key) {
        prefs.remove(key);
    }

    public static void clearAll() {
        try {
            prefs.clear(); // Clears all keys in this preference node
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
