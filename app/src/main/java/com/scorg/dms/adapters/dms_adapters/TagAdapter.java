package com.scorg.dms.adapters.dms_adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by jeetal on 2/3/17.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private Handler mAddedTagsEventHandler;
    private HashMap<String, Object> mAddedTagsForFiltering;
    private ArrayList<String> mTagsDataSet;
    private Context mContext;
    LayoutInflater inflater;
    private Random mRandom = new Random();
    private String TAG = this.getClass().getName();

    public TagAdapter(Context context, HashMap<String, Object> mAddedTagsForFiltering, Handler mAddedTagsEventHandler) {
        mContext = context;
        this.mAddedTagsForFiltering = mAddedTagsForFiltering;
        this.mAddedTagsEventHandler = mAddedTagsEventHandler;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mTagsDataSet = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : mAddedTagsForFiltering.entrySet()) {
            String tempValue;
            if (entry.getValue() instanceof DocTypeList || entry.getValue() instanceof AnnotationList) {
                tempValue = entry.getKey();
            } else {
                tempValue = (entry.getValue().toString() + "|" + entry.getKey());
            }

            if (!DMSConstants.BLANK.equalsIgnoreCase(entry.getValue().toString()))
                mTagsDataSet.add(tempValue);

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageButton mRemoveButton;
        public LinearLayout tagLayout;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_dispalydetails);
            mRemoveButton = (ImageButton) v.findViewById(R.id.ib_remove);
            tagLayout = (LinearLayout) v.findViewById(R.id.tagLayout);
        }
    }

    @Override
    public TagAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = inflater.inflate(R.layout.tag_row_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TagAdapter.ViewHolder holder, int position) {
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setShape(GradientDrawable.RECTANGLE);
        buttonBackground.setColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        buttonBackground.setCornerRadius(mContext.getResources().getDimension(R.dimen.dp20));
        holder.tagLayout.setBackground(buttonBackground);
//        String.valueOf(mTagsDataSet.get(position).split("\\|")[1])

        String data = mTagsDataSet.get(position);

        String dataToShow = "", dataToSet = "";
        if (data.contains("|")) {
            String[] temp = data.split("\\|");
            dataToShow = temp[0];


            //TODO: THIS IS HACK
            //---- To replace data if fileType contains Select:<enteredID> :START
            if (dataToShow.startsWith(mContext.getString(R.string.Select)) && dataToShow.contains(":")) {
                dataToShow = dataToShow.replace(mContext.getString(R.string.Select) + ":", "");
            }

            // THIS IS DONE FOR IF, ONLY FILETYPE SELECTED(IPD/OPD) And no value enter
            if (dataToShow.endsWith(":")) {
                dataToShow = dataToShow.replace(":", "");
            }

            //---------------END

            if (dataToShow.startsWith(mContext.getString(R.string.documenttype))) {
                dataToSet = data;
            } else
                dataToSet = temp[1];
            //---- TO set dataToSet : END

        }
        holder.mTextView.setText(dataToShow);

        holder.mRemoveButton.setTag(dataToSet);


        // Generate a random color
        int color = getRandomHSVColor();

        // Set a random color for TextView background
        // holder.mTextView.setBackgroundColor(getLighterColor(color));

        // Set a text color for TextView
        // holder.mTextView.setTextColor(getReverseColor(color));

        // Set a gradient background for RelativeLayout
        //   holder.mRelativeLayout.setBackground(getGradientDrawable());

        // Emboss the TextView text
        // applyEmbossMaskFilter(holder.mTextView);

        // Set a click listener for TextView
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*String animal = mDataSet.get(position);
                Toast.makeText(mContext,animal,Toast.LENGTH_SHORT).show();*/
            }
        });

        // Set a click listener for item remove button


        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String tag = (String) view.getTag();

                // Remove the item on remove/button click
                mTagsDataSet.remove(holder.getAdapterPosition());

                /*
                    public final void notifyItemRemoved (int position)
                        Notify any registered observers that the item previously located at position
                        has been removed from the data set. The items previously located at and
                        after position may now be found at oldPosition - 1.

                        This is a structural change event. Representations of other existing items
                        in the data set are still considered up to date and will not be rebound,
                        though their positions may be altered.

                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(holder.getAdapterPosition());

                /*
                    public final void notifyItemRangeChanged (int positionStart, int itemCount)
                        Notify any registered observers that the itemCount items starting at
                        position positionStart have changed. Equivalent to calling
                        notifyItemRangeChanged(position, itemCount, null);.

                        This is an item change event, not a structural change event. It indicates
                        that any reflection of the data in the given position range is out of date
                        and should be updated. The items in the given range retain the same identity.

                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(holder.getAdapterPosition(), mTagsDataSet.size());

                mAddedTagsForFiltering.remove(tag);
                mAddedTagsEventHandler.sendMessage(new Message());

                CommonMethods.Log(TAG, mAddedTagsForFiltering.toString());


            }
        });
    }

    @Override
    public int getItemCount() {
        return mTagsDataSet.size();
    }


    // Custom method to apply emboss mask filter to TextView
    protected void applyEmbossMaskFilter(TextView tv) {
        EmbossMaskFilter embossFilter = new EmbossMaskFilter(
                new float[]{1f, 5f, 1f}, // direction of the light source
                0.8f, // ambient light between 0 to 1
                8, // specular highlights
                7f // blur before applying lighting
        );
        // tv.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        //  tv.getPaint().setMaskFilter(embossFilter);
    }

    // Custom method to generate random HSV color
    protected int getRandomHSVColor() {
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        // We make the color depth full
        float saturation = 1.0f;
        // We make a full bright color
        float value = 1.0f;
        // We avoid color transparency
        int alpha = 255;
        // Finally, generate the color
        int color = Color.HSVToColor(alpha, new float[]{hue, saturation, value});
        // Return the color
        return color;
    }

    // Custom method to create a GradientDrawable object
    protected GradientDrawable getGradientDrawable() {
        GradientDrawable gradient = new GradientDrawable();
        gradient.setGradientType(GradientDrawable.SWEEP_GRADIENT);
        gradient.setColors(new int[]{getRandomHSVColor(), getRandomHSVColor(), getRandomHSVColor()});
        return gradient;
    }

    // Custom method to get a darker color
    protected int getDarkerColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 0.8f * hsv[2];
        return Color.HSVToColor(hsv);
    }

    // Custom method to get a lighter color
    protected int getLighterColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = 0.2f + 0.8f * hsv[2];
        return Color.HSVToColor(hsv);
    }

    // Custom method to get reverse color
    protected int getReverseColor(int color) {
        float[] hsv = new float[3];
        Color.RGBToHSV(
                Color.red(color), // Red value
                Color.green(color), // Green value
                Color.blue(color), // Blue value
                hsv
        );
        hsv[0] = (hsv[0] + 180) % 360;
        return Color.HSVToColor(hsv);
    }


    //-- THIS is to get values from mAddedTagsForFiltering, based on tags used.
    public String getUpdatedTagValues(String key, String position) {
        StringBuilder mGeneratedValue = new StringBuilder();
        if (key.startsWith(mContext.getString(R.string.documenttype))) {
            for (Map.Entry<String, Object> entry : mAddedTagsForFiltering.entrySet()) {
                if (entry.getValue() instanceof AnnotationList) {
                    for (DocTypeList docTypeList : ((AnnotationList) entry.getValue()).getDocTypeList())
                        mGeneratedValue.append(docTypeList.getTypeId() + ",");
                } else if (entry.getValue() instanceof DocTypeList)
                    mGeneratedValue.append(((DocTypeList) entry.getValue()).getTypeId() + ",");
            }
        } else {
            Object o = mAddedTagsForFiltering.get(key);
            String s = null;
            if (o != null)
                s = o.toString();

            if (s != null) {
                if (s.contains(":")) {
                    String[] split = s.split(":");
                    if (position != null) {
                        if (split.length > Integer.parseInt(position)) {
                            mGeneratedValue.append(split[Integer.parseInt(position)]);
                        } else {
                            mGeneratedValue.append(DMSConstants.BLANK);
                        }
                    } else {
                        mGeneratedValue.append(split[1]);
                    }
                } else {
                    mGeneratedValue.append(s);
                }
            } else {
                mGeneratedValue.append(DMSConstants.BLANK);
            }
        }

        CommonMethods.Log(TAG, "" + mGeneratedValue);
        CommonMethods.Log(TAG, "" + mGeneratedValue.toString());
        return mGeneratedValue.toString();
    }
}
