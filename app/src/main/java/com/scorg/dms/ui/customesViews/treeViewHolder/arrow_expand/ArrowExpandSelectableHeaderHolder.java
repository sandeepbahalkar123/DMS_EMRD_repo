package com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.ArchiveDatum;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDateFileType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDateFolderType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocCategory;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocType;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstHideDocType;
import com.scorg.dms.singleton.DMSApplication;
import com.unnamed.b.atv.model.TreeNode;

/**
 *
 */
public class ArrowExpandSelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<ArrowExpandIconTreeItemHolder.IconTreeItem> {
    private int nodeValueColor;
    private boolean isTreeLabelBold;
    private int leftPadding;
    private boolean isDefaultExpanded;
    private boolean istViewClickRequired;
    private TextView tvValue;
    private boolean isOnlyOneNodeExpanded;
    private ImageView arrowView, icon_lock;
    private AppCompatCheckBox nodeSelector;
    private LinearLayout mainContentLayout;
    private LinearLayout mainContentBackground;
    private int confidentialState;
    private ArrowExpandSelectableHeaderHolderLockIconClickListener lockIconClickListener;
    private boolean isChecked;

    public ArrowExpandSelectableHeaderHolder(Context context, boolean isDefaultExpanded, boolean istViewClickRequired, int confidentialState, boolean isChecked) {

        this(context, isDefaultExpanded, (int) (context.getResources().getDimension(R.dimen.dp10) / context.getResources().getDisplayMetrics().density), istViewClickRequired, confidentialState, isChecked);
    }

    public ArrowExpandSelectableHeaderHolder(Context context, boolean isDefaultExpanded, int leftPadding, boolean istViewClickRequired, int confidentialState, boolean isChecked) {
        super(context);
        this.leftPadding = leftPadding;
        this.isDefaultExpanded = isDefaultExpanded;
        this.istViewClickRequired = istViewClickRequired;
        this.confidentialState = confidentialState;
        this.isChecked = isChecked;
        nodeValueColor = ContextCompat.getColor(context, R.color.black);

        if (context instanceof ArrowExpandSelectableHeaderHolderLockIconClickListener) {
            lockIconClickListener = (ArrowExpandSelectableHeaderHolderLockIconClickListener) context;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View createNodeView(final TreeNode node, final ArrowExpandIconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_arrow_expandable_header, null, false);
        nodeSelector = view.findViewById(R.id.node_selector);


        int[][] states = new int[][]{
                new int[]{android.R.attr.state_enabled}, // enabled
                new int[]{-android.R.attr.state_enabled}, // disabled
                new int[]{-android.R.attr.state_checked}, // unchecked
                new int[]{android.R.attr.state_pressed} // pressed
        };

        int[] colors = new int[]{
                Color.parseColor(DMSApplication.COLOR_PRIMARY),
                Color.parseColor(DMSApplication.COLOR_PRIMARY),
                Color.parseColor(DMSApplication.COLOR_PRIMARY),
                Color.parseColor(DMSApplication.COLOR_PRIMARY)
        };
        ColorStateList myList = new ColorStateList(states, colors);
        nodeSelector.setSupportButtonTintList(myList);


        icon_lock = view.findViewById(R.id.icon_lock);
        icon_lock.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        arrowView = view.findViewById(R.id.icon);
        arrowView.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mainContentLayout = view.findViewById(R.id.mainContentLayout);
        mainContentBackground = view.findViewById(R.id.mainContentBackground);

        mainContentLayout.setPadding(leftPadding, 0, 0, 0);

        tvValue = view.findViewById(R.id.node_value);
        tvValue.setTextColor(getNodeValueColor());

        if (isTreeLabelBold())
            tvValue.setTypeface(null, Typeface.BOLD);
        else
            tvValue.setTypeface(null, Typeface.NORMAL);

        if (value.text.toString().contains("|")) {
            tvValue.setText(value.text.toString().split("\\|")[0]);
        } else {
            tvValue.setText(value.text.toString());
        }


        //arrowView.setPadding(20, 10, 10, 10);
        if (node.isLeaf()) {
            arrowView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tree_file));
            arrowView.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        }


        icon_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confidentialState == 2 || confidentialState == 3) {
                    lockIconClickListener.onLockIconClick(node.getValue(), true);
                }
            }
        });

        arrowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tView.toggleNode(node, isOnlyOneNodeExpanded());
            }
        });

        if (istViewClickRequired) {
            tvValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tView.toggleNode(node, isOnlyOneNodeExpanded());
                }
            });
        }

        nodeSelector.setClickable(false);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
                if (node.isLeaf()) {
                    int totalBrother = node.getParent().getChildren().size();
                    int checkedCount = 0;
                    for (TreeNode treeNode : node.getParent().getChildren()) {
                        if (treeNode.isSelected())
                            checkedCount += 1;
                    }

                    if (isChecked) {
                        if (checkedCount == totalBrother) {
                            getTreeView().selectNode(node.getParent(), isChecked);
                        }
                    } else {
                        if (checkedCount == totalBrother - 1) {
                            getTreeView().selectNode(node.getParent(), isChecked);
                        }
                    }
                }
                nodeSelector.setChecked(node.isSelected());

            }
        });

        if (isOnlyOneNodeExpanded()) {
            if (node.isFirstChild()) {
                node.setExpanded(isDefaultExpanded);
            }
        } else {
            node.setExpanded(isDefaultExpanded);
        }

        if (value.objectData instanceof DocTypeList) {
            node.setSelected(((DocTypeList) value.objectData).
                    getSelected());
            nodeSelector.setChecked(((DocTypeList) value.objectData).
                    getSelected());
        }

        if (value.objectData instanceof AnnotationList) {
            node.setSelected(((AnnotationList) value.objectData).
                    getSelected());
            nodeSelector.setChecked(((AnnotationList) value.objectData).
                    getSelected());
        }

        if (value.objectData instanceof ArchiveDatum) {
            tvValue.setTextColor(Color.parseColor(((ArchiveDatum) value.objectData).getNodeColor()));
            mainContentBackground.setBackgroundColor(Color.parseColor(((ArchiveDatum) value.objectData).getFavouriteColor()));
        } else if (value.objectData instanceof LstDocType) {
            tvValue.setTextColor(Color.parseColor(((LstDocType) value.objectData).getNodeColor()));
            mainContentBackground.setBackgroundColor(Color.parseColor(((LstDocType) value.objectData).getFavouriteColor()));

            // Favorite logic -----------------------------
//            if (node.isLeaf()) {
//                if (((LstDocType) value.objectData).isFavourite()) {
//                    node.setExpanded(true);
//                    if (node.getParent() != null)
//                        node.getParent().setExpanded(true);
//                    if (node.getRoot() != null)
//                        node.getRoot().setExpanded(true);
//                }
//            }
            // Favorite logic -----------------------------

        } else if (value.objectData instanceof LstDateFileType) {
            tvValue.setTextColor(Color.parseColor(((LstDateFileType) value.objectData).getNodeColor()));
            mainContentBackground.setBackgroundColor(Color.parseColor(((LstDateFileType) value.objectData).getFavouriteColor()));
        } else if (value.objectData instanceof LstDateFolderType) {
            tvValue.setTextColor(Color.parseColor(((LstDateFolderType) value.objectData).getNodeColor()));
            mainContentBackground.setBackgroundColor(Color.parseColor(((LstDateFolderType) value.objectData).getFavouriteColor()));
        } else if (value.objectData instanceof LstDocCategory) {
            tvValue.setTextColor(Color.parseColor(((LstDocCategory) value.objectData).getNodeColor()));
            mainContentBackground.setBackgroundColor(Color.parseColor(((LstDocCategory) value.objectData).getFavouriteColor()));
        } else if (value.objectData instanceof LstHideDocType) {
            tvValue.setTextColor(Color.parseColor(((LstHideDocType) value.objectData).getNodeColor()));
            mainContentBackground.setBackgroundColor(Color.parseColor(((LstHideDocType) value.objectData).getFavouriteColor()));
        }

        if (confidentialState == 2 || confidentialState == 3 || confidentialState == 4) {
            icon_lock.setVisibility(View.VISIBLE);
            if (confidentialState == 4) {
                icon_lock.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlock));
                icon_lock.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
            }
        }

        if (isChecked) {
            nodeSelector.setChecked(isChecked);
        }

        return view;
    }


    @Override
    public void toggle(boolean active) {
        //  arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        if (!mNode.isLeaf()) {
            arrowView.setImageDrawable(context.getResources().getDrawable(active ? R.drawable.ic_tree_folder_open : R.drawable.ic_tree_close_folder));
            arrowView.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        }

    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.INVISIBLE);
        icon_lock.setVisibility(editModeEnabled ? View.INVISIBLE : View.VISIBLE);
        nodeSelector.setChecked(mNode.isSelected());
    }

    public int getNodeValueColor() {
        return nodeValueColor;
    }

    public void setNodeValueColor(int nodeValueColor) {
        this.nodeValueColor = nodeValueColor;
    }

    public boolean isTreeLabelBold() {
        return isTreeLabelBold;
    }

    public void setTreeLabelBold(boolean treeLabelBold) {
        isTreeLabelBold = treeLabelBold;
    }

    public boolean isOnlyOneNodeExpanded() {
        return isOnlyOneNodeExpanded;
    }

    public void setOnlyOneNodeExpanded(boolean expandedOrCollapsed) {
        isOnlyOneNodeExpanded = expandedOrCollapsed;
    }

    public void setCheckBoxColor(CheckBox checkBox, int checkedColor, int uncheckedColor) {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {checkedColor, uncheckedColor};
        CompoundButtonCompat.setButtonTintList(checkBox, new
                ColorStateList(states, colors));
    }


    public interface ArrowExpandSelectableHeaderHolderLockIconClickListener {
        void onLockIconClick(Object value, boolean isLeaf);
    }
}
