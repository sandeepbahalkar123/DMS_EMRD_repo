package com.rescribe.doctor.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sandeep Bahalkar
 */
public class RescribePreferencesManager {

    private static final String TAG = "DMS/DmsPreferencesManager";
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
        //String mKey = encrypt(key);
        //String mValue = encrypt(value);
//        CommonMethods.Log(TAG, "putString--mKey--->" + key + "<----mValue-------->" + value);
        getSharedPreference(context).edit().putString(key, value).apply();
    }

    public static void putLong(String key, long value, Context context) {
        //String mKey = encrypt(key);
        // String mValue = encrypt(Ln.toString(value));
        getSharedPreference(context).edit().putLong(key, value).apply();
    }

    public static void putInt(String key, int value, Context context) {
//        String mKey = encrypt(key);
        // String mValue = encrypt(Integer.toString(value));
        getSharedPreference(context).edit().putInt(key, value).apply();
    }

    public static int getInt(String key, Context context) {
//        String mKey = encrypt(key);
        /*CommonMethods.Log("value", "value--getBoolean----->" + mKey);
        final String encryptedValue = getSharedPreference(context).getString(mKey, "");
        if (encryptedValue == null || encryptedValue.equals("")) {
            return 0;
        }
        try {
            return Integer.parseInt(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }*/

        return getSharedPreference(context).getInt(key, 0);

    }

    public static long getLong(String key, Context context) {
       /* String mKey = encrypt(key);
        CommonMethods.Log("value", "value--getBoolean----->" + mKey);
        final String encryptedValue = getSharedPreference(context).getLong(mKey, "");
        if (encryptedValue == null || encryptedValue.equals("")) {
            return 0;
        }
        try {
//            return Integer.parseInt(decrypt(encryptedValue));
            return Long.valueOf(decrypt(encryptedValue));
        } catch (NumberFormatException e) {
            throw new ClassCastException(e.getMessage());
        }*/

        return getSharedPreference(context).getLong(key, 0);

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

    private static String encrypt(String cleartext) {
        if (cleartext == null || cleartext.length() == 0) {
            return cleartext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(RescribePreferencesManager.sKey, "AES"));
            return encode(cipher.doFinal(cleartext.getBytes("UTF-8")));
        } catch (Exception e) {
            Log.e(RescribePreferencesManager.class.getName(), "encrypt", e);
            return null;
        }
    }

    private static String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.length() == 0) {
            return ciphertext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(RescribePreferencesManager.sKey, "AES"));
            return new String(cipher.doFinal(RescribePreferencesManager.decode(ciphertext)), "UTF-8");
        } catch (Exception e) {
            Log.e(RescribePreferencesManager.class.getName(), "decrypt", e);
            return null;
        }
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

        String EMAIL = "email";
        String AUTHTOKEN = "authToken";
        String DOC_ID = "docId";
        String LOGIN_STATUS = "login_status";
        String MOBILE_NUMBER = "mobileNumber";
        String LOGIN_OR_SIGNUP = "signup_login";
        String COACHMARK = "coachmark";
        String MOBILE_NUMBER_GMAIL = "mobile_number_gmail";
        String PASSWORD_GMAIL = "password_gmail";
        String MOBILE_NUMBER_FACEBOOK = "mobile_number_facebook";
        String PASSWORD_FACEBOOK = "password_facebook";
        String PROFILE_PHOTO = "doctorprofile";
        String SPECIALITY = "speciality";
        String ADDRESS = "address";
        String IS_EXIT = "isExit";
        String PATIENT_ID = "patient_id";
        String CHAT_IS_CHECKED = "is_checked";
        String DOC_INFO = "doc_info";
        String SELECTED_LOCATION_ID = "selected_location_id";
        String BACK_UP = "back_up";
        String PATIENT_DOWNLOAD = "patient_download";
        String COACHMARK_GET_TOKEN = "coachmark_get_token";

        String COACHMARK_ALL_PATIENT_DOWNLOAD = "COACHMARK_ALL_PATIENT_DOWNLOAD";

        String isSkippedClicked = "is_skipped";
        String isUpdatedClicked = "is_updated_clicked";
        String isLaterClicked = "is_later_clicked";
        String VERSION_CODE_FROM_SERVER = "version_code_from_server";
        String SHOW_UPDATE_DIALOG = "show_update_dialog";
        String SHOW_UPDATE_DIALOG_ON_SKIPPED = "show_update_dialog_on_skipped";
        String ADD_PATIENT_OFFLINE_SETTINGS = "isAddPatientOfflineSetting";


        String SERVER_PATH = "server_path";
        String USER_GENDER = "user_gender";
        String SERVER_CONNECTION_SUCCESS = "success";
        String IS_VALID_IP_CONFIG = "isvalidipconfig";
        String USER_NAME = "user_name";
        String PASSWORD = "password";

    }
}