package com.scorg.dms.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.CheckIpConnection;
import com.scorg.dms.interfaces.DatePickerDialogListener;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.SplashScreenActivity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CommonMethods {

    private static final String TAG = "DOCUPHI/CommonMethods";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static boolean encryptionIsOn = true;
    private static String aBuffer = "";
    private static CheckIpConnection mCheckIpConnection;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatePickerDialogListener mDatePickerDialogListener;

    public static void showToast(Context context, String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    public static String toCamelCase(String input) {
        if (input == null)
            return "null";

        if (!input.equals("")) {
            StringBuilder result = new StringBuilder();
            char firstChar = input.charAt(0);
            result.append(Character.toUpperCase(firstChar));
            for (int i = 1; i < input.length(); i++) {
                char currentChar = input.charAt(i);
                char previousChar = input.charAt(i - 1);
                if (previousChar == ' ')
                    result.append(Character.toUpperCase(currentChar));
                else
                    result.append(Character.toLowerCase(currentChar));
            }
            return result.toString();
        } else {
            return input;
        }
    }


    public static void showSnack(View mViewById, String msg) {
        if (mViewById != null) {
            Snackbar.make(mViewById, msg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Log.d(TAG, "null snacbar view" + msg);
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    /**
     * Email validator
     *
     * @param emailId
     * @return
     */
    public final static boolean isValidEmail(CharSequence emailId) {
        if (emailId == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();
        }
    }


    public static void hideKeyboard(Activity cntx) {
        // Check if no view has focus:
        View view = cntx.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) cntx.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static TextDrawable getTextDrawable(Context context, String name) {
        ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
        name = nameInitialSuffix(name);
        if (DMSConstants.BLANK.equalsIgnoreCase(name)) {
            int color2 = mColorGenerator.getColor(name);
            return TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(context.getResources().getDimension(R.dimen.dp67))) // width in px
                    .height(Math.round(context.getResources().getDimension(R.dimen.dp67))) // height in px
                    .endConfig()
                    .buildRound(("" + name).toUpperCase(), color2);
        } else {
            int color2 = mColorGenerator.getColor(name);
            return TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(context.getResources().getDimension(R.dimen.dp67))) // width in px
                    .height(Math.round(context.getResources().getDimension(R.dimen.dp67))) // height in px
                    .endConfig()
                    .buildRound(("" + name.charAt(0)).toUpperCase(), color2);
        }

    }

    private static String nameInitialSuffix(String name) {
        if (name.substring(0, 3).equals("Mrs")) {
            name = name.substring(4, name.length());
        } else if (name.substring(0, 4).equals("Mrs.")) {
            name = name.substring(5, name.length());
        } else if (name.substring(0, 2).equals("Mr") || name.substring(0, 2).equals("Dr")) {
            name = name.substring(3, name.length());
        } else if (name.substring(0, 3).equals("Mr.") || name.substring(0, 3).equals("Dr.")) {
            name = name.substring(4, name.length());
        } else if (name.substring(0, 4).equals("Miss")) {
            name = name.substring(5, name.length());
        } else if (name.substring(0, 5).equals("Miss.")) {
            name = name.substring(6, name.length());
        } else if (name.charAt(0) == ' ') {
            name = name.substring(1, name.length());
        }
        return name;
    }

    public static int getVersionCode(Context mContext) {
        int versionCode = -1;
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String getVersionName(Context mContext) {
        String versionName = "";
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static String getCurrentDate(String yyyyMmDd) // for enrollmentId
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(yyyyMmDd, Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));/// this line add to set UTC time zone to date
        return df.format(c.getTime());
    }

    public static void showSnack(Context mContext, View mViewById, String msg) {
        Snackbar snack = Snackbar.make(mViewById, msg, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(mContext.getResources().getColor(R.color.errorColor));
        snack.show();
    }

    public static void Log(String tag, String message) {
        Log.e(tag, "" + message);
    }


    public static String getFormattedDate(String strDate, String sourceFormat, String destinyFormat) {
        if (!strDate.equals("")) {

            SimpleDateFormat df;
            df = new SimpleDateFormat(sourceFormat, Locale.US);
            Date date;
            try {
                date = df.parse(strDate);
                df = new SimpleDateFormat(destinyFormat, Locale.US);
                return df.format(date);

            } catch (ParseException e) {
                return "";
            }
        } else return "";
    }

    /**
     * The method will return the date and time in requested format
     *
     * @param selectedDateTime to be converted to requested format
     * @param requestedFormat  the format in which the provided datetime needs to be changed
     * @param formatString     differentiate parameter to format date or time
     * @return formated date or time
     */
    public static String formatDateTime(String selectedDateTime, String requestedFormat, String currentDateFormat, String formatString) {
        String ourDate = "";
        try {
            SimpleDateFormat ft = new SimpleDateFormat(currentDateFormat, Locale.US);
            ft.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = ft.parse(selectedDateTime);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(requestedFormat, Locale.US); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            ourDate = dateFormatter.format(value);
        } catch (Exception e) {
            if (formatString.equalsIgnoreCase(DMSConstants.TIME)) {
                e.fillInStackTrace();
                ourDate = "00:00 am";
            } else if (formatString.equalsIgnoreCase(DMSConstants.DATE)) {
                e.fillInStackTrace();
                ourDate = "00-00-000";
            }
        }
        return ourDate;
    }


    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static Date convertStringToDate(String dateString, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;

        try {
            date = formatter.parse(dateString.trim());
        } catch (ParseException e) {
            e.printStackTrace();
            CommonMethods.Log("convertStringToDate", "convertStringToDate EXCEPTION OCCURS : " + e.getMessage());
        }
        return date;
    }

    public static String getSuffixForNumber(final int n) {
        //  checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }

    }


    //this alert is shown for input of serverpath
    public static Dialog showIPAlertDialog(Context activity, String dialogHeader, CheckIpConnection checkIpConnection) {
        final Context mContext = activity;
        mCheckIpConnection = checkIpConnection;
        final Dialog dialog = new Dialog(activity);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ok_cancel);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        if (dialogHeader != null)
            ((TextView) dialog.findViewById(R.id.textView_dialog_heading)).setText(dialogHeader);
        Button buttonRight = dialog.findViewById(R.id.button_cancel);
        Button buttonLeft = dialog.findViewById(R.id.button_ok);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etServerPath = (EditText) dialog.findViewById(R.id.et_server_path);

                if (isValidIP(etServerPath.getText().toString())) {
                    String mServerPath = Config.HTTP + etServerPath.getText().toString();// + Config.API;
                    Log.e(TAG, "SERVER PATH===" + mServerPath);
                    mCheckIpConnection.onOkButtonClickListner(mServerPath, mContext, dialog);
                } else {
                    Toast.makeText(mContext, R.string.error_in_ip, Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) mContext).finish();
            }
        });
        dialog.show();

        return dialog;
    }

    public static void showDialog(String msg, String changeIpAddress, final Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        float[] bottomLeftRadius = {0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8)};
        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomLeftRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomRightRadius);

        Button buttonRight = dialog.findViewById(R.id.button_cancel);
        Button buttonLeft = dialog.findViewById(R.id.button_ok);
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        dialogIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        buttonLeft.setBackground(buttonLeftBackground);
        buttonRight.setBackground(buttonRightBackground);

        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(msg + changeIpAddress);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DMSPreferencesManager.clearSharedPref(mContext);
                ((Activity) mContext).finish();
                mContext.startActivity(new Intent(mContext, SplashScreenActivity.class));
                ((AppCompatActivity) mContext).finishAffinity();

            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public static void showErrorDialog(String msg, final Context mContext, boolean isTimeout, final ErrorDialogCallback errorDialogCallback) {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        float[] bottomLeftRadius = {0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8)};
        float[] bottomLiftRightRadius = {0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8)};


        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomLeftRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomRightRadius);


        GradientDrawable buttonLeftRightBackground = new GradientDrawable();
        buttonLeftRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftRightBackground.setCornerRadii(bottomLiftRightRadius);


        Button buttonCancel = dialog.findViewById(R.id.button_cancel);
        Button buttonOk = dialog.findViewById(R.id.button_ok);
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        dialogIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        buttonCancel.setText(R.string.retry);
        View space = dialog.findViewById(R.id.dialogSpace);

        buttonOk.setBackground(buttonLeftRightBackground);
        buttonCancel.setBackground(buttonRightBackground);
        if (isTimeout) {
            buttonCancel.setVisibility(View.VISIBLE);
            buttonOk.setBackground(buttonLeftBackground);
        } else {
            space.setVisibility(View.GONE);
            buttonCancel.setVisibility(View.GONE);
            buttonOk.setBackground(buttonLeftRightBackground);
        }

        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(msg);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialogCallback.ok();
                dialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialogCallback.retry();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private static boolean isValidIP(String ipAddr) {

        Pattern ptn = Pattern.compile("(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\.(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\.(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\.(\\b(1?[0-9]{1,2}|2[0-4][0-9]|25[0-5])\\b)\\:(\\d{1,4})$");
        Matcher mtch = ptn.matcher(ipAddr);
        return mtch.find();
    }

    public static View loadView(int resourceName, Context mActivity) {

        LayoutInflater inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // param.gravity = Gravity.CENTER;
        View child = inflater.inflate(resourceName, null);
        LinearLayout l1 = new LinearLayout(mActivity);
        child.setLayoutParams(param);

        l1.setLayoutParams(param);
        l1.addView(child);
        return l1;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    //--------------

    /**
     * This use to decrypt received pdfFile path using AES Algo
     * Step 1: Converted received encrypted text to hex array
     * Step 2: Create Cipher class object based on AES/CBC/nopadding
     * Step 3: Decrypt text
     * Step 4: Create String object with UTF-8 encoding
     *
     * @param encryptedText
     * @return
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decryptPDFFilePathUsingAESAlgo(String encryptedText) throws GeneralSecurityException, IOException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        //key byte array
        byte[] sKeyArray = new byte[]{(byte) 113, (byte) 217, (byte) 19, (byte) 11, (byte) 24, (byte) 26,
                (byte) 85, (byte) 45, (byte) 194, (byte) 184, (byte) 27, (byte) 162, (byte) 37, (byte) 112, (byte) 222, (byte) 209, (byte) 241,
                (byte) 24, (byte) 175, (byte) 154, (byte) 173, (byte) 53, (byte) 196, (byte) 29, (byte) 24, (byte) 26, (byte) 17,
                (byte) 218, (byte) 131, (byte) 236, (byte) 53, (byte) 209};

        //vector byte array
        byte[] vectorOrIvArray = new byte[]{(byte) 146, (byte) 64, (byte) 91, (byte) 111, (byte) 23, (byte) 3, (byte) 213,
                (byte) 119, (byte) 231, (byte) 121, (byte) 252, (byte) 112, (byte) 79, (byte) 32, (byte) 114, (byte) 156};


        //STEP 1
        byte[] value_bytes = hexStringToByteArray(encryptedText);
        //STEP 2
        Cipher localCipher = Cipher.getInstance("AES/CBC/nopadding");

        //STEP 3
        localCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sKeyArray, "AES"), new IvParameterSpec(vectorOrIvArray));
        byte[] decryptBytes = localCipher.doFinal(value_bytes);

        //STEP 4
        return new String(decryptBytes, "UTF-8");
    }

    private static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    @SuppressLint("CheckResult")
    public static void setImageUrl(Context mContext, String imageName, ImageView imageView, int defaultImage) {
        String url = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext) + DMSConstants.Images.FOLDER + DMSApplication.RESOLUTION + imageName;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(defaultImage);
        requestOptions.placeholder(defaultImage);
        requestOptions.signature(new ObjectKey(imageName + DMSPreferencesManager.getString(DMSPreferencesManager.CACHE_TIME, mContext)));
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
        CommonMethods.Log(TAG, url);
    }

    @SuppressLint("CheckResult")
    public static void setBackgroundImageUrl(Context mContext, String imageName, LinearLayout imageView, int defaultImage) {
        String url = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext) + DMSConstants.Images.FOLDER + DMSApplication.RESOLUTION + imageName;

        BitmapDrawable d = new BitmapDrawable(url);
        imageView.setBackground(d);
    }

    public static boolean isNullOrEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    //--------------
}

