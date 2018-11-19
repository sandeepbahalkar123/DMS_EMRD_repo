package com.scorg.dms.ui.customesViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.util.CommonMethods;

import java.lang.reflect.Field;

public class SearchTextViewWithDeleteButton extends LinearLayout {
    protected AutoCompleteTextView editText;
    protected ImageButton clearTextButton;
    private SearchTextViewWithDeleteButton.OnClearButtonClickedInEditTextListener mClearButtonClickedInEditTextListener;

    public void setHint(String string) {
        editText.setHint(string);
    }

    public interface TextChangedListener extends TextWatcher {
    }

    SearchTextViewWithDeleteButton.TextChangedListener editTextListener = null;

    public void addTextChangedListener(SearchTextViewWithDeleteButton.TextChangedListener listener) {
        this.editTextListener = listener;
    }

    public SearchTextViewWithDeleteButton(Context context) {
        super(context);
        //LayoutInflater.from(context).inflate(R.layout.activity_main, this); // Previosuly uncommented, I commented this still working properly.
    }

    public SearchTextViewWithDeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public SearchTextViewWithDeleteButton(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.EditTextWithDeleteButton, 0, 0);
        String hintText;
        int deleteButtonRes;
        float textSize = 14;
        try {
            // get the text and colors specified using the names in attrs.xml
            hintText = a.getString(R.styleable.EditTextWithDeleteButton_hintText);
            deleteButtonRes = a.getResourceId(
                    R.styleable.EditTextWithDeleteButton_deleteButtonRes,
                    R.drawable.icon_text_field_clear_btn);

            textSize = a.getDimension(
                    R.styleable.EditTextWithDeleteButton_textSize,
                    14);


        } finally {
            a.recycle();
        }
        editText = createEditText(context, hintText, textSize);
        clearTextButton = createImageButton(context, deleteButtonRes);

        this.addView(editText);
        this.addView(clearTextButton);
        editText.addTextChangedListener(txtEntered());


        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editText.getText().toString().length() > 0)
                    clearTextButton.setVisibility(View.VISIBLE);
                else
                    clearTextButton.setVisibility(View.VISIBLE);

            }
        });
        clearTextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mClearButtonClickedInEditTextListener != null)
                    mClearButtonClickedInEditTextListener.onClearButtonClicked();

                editText.setText("");
            }
        });
    }

    public TextWatcher txtEntered() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (editTextListener != null)
                    editTextListener.onTextChanged(s, start, before, count);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextListener != null)
                    editTextListener.afterTextChanged(s);
                if (editText.getText().toString().length() > 0)
                    clearTextButton.setVisibility(View.VISIBLE);
                else
                    clearTextButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (editTextListener != null)
                    editTextListener.beforeTextChanged(s, start, count, after);

            }

        };
    }

    @SuppressLint("NewApi")
    private AutoCompleteTextView createEditText(Context context, String hintText, float textSize) {
        editText = new AutoCompleteTextView(context);
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        editText.setLayoutParams(new TableLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.2f));
        editText.setTextColor(ContextCompat.getColor(context, R.color.white));
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        editText.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.dp6));
        editText.setHintTextColor(ContextCompat.getColor(context, R.color.hint_color_my_appointment));
        editText.setHorizontallyScrolling(false);
        editText.setPadding((int) getResources().getDimension(R.dimen.dp12), (int) getResources().getDimension(R.dimen.dp8), 0, 0);
        editText.setVerticalScrollBarEnabled(true);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        editText.setBackground(null);
        editText.setHint(hintText);
        editText.setDropDownAnchor(getId());
        editText.setDropDownVerticalOffset((int) getResources().getDimension(R.dimen.dp8));
        //editText.setDropDownHorizontalOffset((int) getResources().getDimension(R.dimen.dp8));

        //--------
        try {
            Field f = null;
            f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);

            f.set(editText, R.drawable.cursor_color_white);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //--------
        return editText;
    }

    private ImageButton createImageButton(Context context, int deleteButtonRes) {
        clearTextButton = new ImageButton(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        params.gravity = Gravity.CENTER_VERTICAL;
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dp3), getResources().getDimensionPixelSize(R.dimen.dp12), 0);
        clearTextButton.setLayoutParams(params);
        clearTextButton.setPadding(CommonMethods.convertDpToPixel(R.dimen.dp3), CommonMethods.convertDpToPixel(R.dimen.dp3), CommonMethods.convertDpToPixel(R.dimen.dp3), CommonMethods.convertDpToPixel(R.dimen.dp3));
        clearTextButton.setBackgroundResource(deleteButtonRes);
        return clearTextButton;
    }

    public Editable getText() {
        Editable text = editText.getText();
        return text;
    }

    public void setText(String text) {
        editText.setText(text);
    }

    public interface OnClearButtonClickedInEditTextListener {
        void onClearButtonClicked();
    }

    public void addClearTextButtonListener(SearchTextViewWithDeleteButton.OnClearButtonClickedInEditTextListener onClearButtonClickedInEditTextListener) {
        this.mClearButtonClickedInEditTextListener = onClearButtonClickedInEditTextListener;
    }

    public AutoCompleteTextView getEditText() {
        return editText;
    }
}
