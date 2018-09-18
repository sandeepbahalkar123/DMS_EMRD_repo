package com.scorg.dms.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sandeep Bahalkar
 */
public class DMSPreferencesManager {

    private static final String TAG = "DMS/DmsPreferencesManager";
    public static final String CACHE_TIME = "cache_time";
    private static SharedPreferences sharedPreferences = null;
    private static byte[] sKey;

    public static SharedPreferences getSharedPreference(final Context context) {
        if (context != null) {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                try {
                    final String key = generateAesKeyName(context);
                    String value = sharedPreferences.getString(key, null);
                    if (value == null) {
                        value = generateAesKeyValue();
                        sharedPreferences.edit().putString(key, value).apply();
                    }
                    sKey = decode(value);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            }
        }
        return sharedPreferences;
    }

    /*public static ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(sharedPreferences.getString(key, ""), ",=,")));
    }*/


    /*public static void putListString(String key, ArrayList<String> stringList, Context mContext) {
        if (key == null) return;
        if (stringList ==null)return;
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        getSharedPreference(mContext).edit().putString(key, TextUtils.join(",=,", myStringList)).apply();
    }*/

    public static String getString(String key, Context context) {
        //String mKey = encrypt(key);
//        CommonMethods.Log(TAG, "getString--mKey--->" + key + "<----mValue-------->" + getSharedPreference(context).getString(key, ""));
        return getSharedPreference(context).getString(key, "");

    }

    public static boolean getBoolean(String key, Context context) {
        //String mKey = encrypt(key);
        /*CommonMethods.Log("value", "value--getBoolean----->" + mKey);
        final String encryptedValue = getSharedPreference(context).getString(mKey, "");
        if (encryptedValue == null || encryptedValue.equals("")) {
            return false;
        }
        try {
            return Boolean.parseBoolean(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }*/
        return getSharedPreference(context).getBoolean(key, false);

    }

    public static boolean putBoolean(String key, boolean value, Context context) {
        //String mKey = encrypt(key);
        // String mValue = encrypt(Boolean.toString(value));
//        CommonMethods.Log(TAG, "--------------------------" + Boolean.toString(value));
        //getSharedPreference(context).edit().putString(mKey, encrypt(mValue)).commit();
        getSharedPreference(context).edit().putBoolean(key, value).apply();
        return value;
    }

    public static void putString(String key, String value, Context context) {
        getSharedPreference(context).edit().putString(key, value).apply();
    }


    public static void putInt(String key, int value, Context context) {
        getSharedPreference(context).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, Context context) {
        return getSharedPreference(context).getInt(key, 0);

    }

    public static void clearSharedPref(Context context) {
        getSharedPreference(context).edit().clear().apply();
    }

    private static String encode(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static byte[] decode(String input) {
        return Base64.decode(input, Base64.NO_PADDING | Base64.NO_WRAP);
    }

    private static String generateAesKeyValue() throws NoSuchAlgorithmException {
        // Do *not* seed secureRandom! Automatically seeded from system entropy
        final SecureRandom random = new SecureRandom();

        // Use the largest AES key length which is supported by the OS
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        try {
            generator.init(256, random);
        } catch (Exception e) {
            try {
                generator.init(192, random);
            } catch (Exception e1) {
                generator.init(128, random);
            }
        }
        return encode(generator.generateKey().getEncoded());
    }

    private static String generateAesKeyName(Context context) throws InvalidKeySpecException,
            NoSuchAlgorithmException {
        final char[] password = context.getPackageName().toCharArray();
        final byte[] salt = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID).getBytes();

        // Number of PBKDF2 hardening rounds to use, larger values increase
        // computation time, you should select a value that causes
        // computation to take >100ms
        final int iterations = 1000;

        // Generate a 256-bit key
        final int keyLength = 256;

        final KeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        return encode(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                .generateSecret(spec).getEncoded());
    }


    public interface DMS_PREFERENCES_KEY {

        String DOC_ID = "docId";
        String DOC_NAME = "doctorName";
        String HOSPITAL_NAME = "hospitalName";
        String IS_EXIT = "isExit";
        String isSkippedClicked = "is_skipped";
        String isLaterClicked = "is_later_clicked";
        String VERSION_CODE_FROM_SERVER = "version_code_from_server";
        String SHOW_UPDATE_DIALOG = "show_update_dialog";
        String SHOW_UPDATE_DIALOG_ON_SKIPPED = "show_update_dialog_on_skipped";
        String SERVER_PATH = "server_path";
        String USER_GENDER = "user_gender";
        String IS_VALID_IP_CONFIG = "isvalidipconfig";
        String USER_NAME = "user_name";
        String PASSWORD = "password";
        String ARCHIVE_API_COUNT = "ViewArchivedApiTakeCount";

    }
}