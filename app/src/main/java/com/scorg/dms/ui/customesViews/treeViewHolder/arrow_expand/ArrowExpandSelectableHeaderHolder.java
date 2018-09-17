package com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.PatientEpisodeRecycleViewListAdapter;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.util.CommonMethods;
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
    private CheckBox nodeSelector;
    private LinearLayout mainContentLayout;
    private int confidentialState;
    private ArrowExpandSelectableHeaderHolderLockIconClickListener lockIconClickListener;
    private boolean isChecked;

    public ArrowExpandSelectableHeaderHolder(Context context, boolean isDefaultExpanded, boolean istViewClickRequired, int confidentialState,boolean isChecked) {

        this(context, isDefaultExpanded, (int) (context.getResources().getDimension(R.dimen.dp10) / context.getResources().getDisplayMetrics().density), istViewClickRequired, confidentialState,isChecked);
    }

    public ArrowExpandSelectableHeaderHolder(Context context, boolean isDefaultExpanded, int leftPadding, boolean istViewClickRequired, int confidentialState,boolean isChecked) {
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

    @Override
    public View createNodeView(final TreeNode node, final ArrowExpandIconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_arrow_expandable_header, null, false);

        mainContentLayout = (LinearLayout) view.findViewById(R.id.mainContentLayout);
        icon_lock = (ImageView) view.findViewById(R.id.icon_lock);
        mainContentLayout.setPadding(leftPadding, 0, 0, 0);

        tvValue = (TextView) view.findViewById(R.id.node_value);
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


        arrowView = (ImageView) view.findViewById(R.id.icon);
        //arrowView.setPadding(20, 10, 10, 10);
        if (node.isLeaf()) {
            arrowView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tree_file));
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
                if (isOnlyOneNodeExpanded()) {
                    tView.toggleNode(node, isOnlyOneNodeExpanded());

                } else {
                    tView.toggleNode(node, isOnlyOneNodeExpanded());
                }
            }
        });
        if (istViewClickRequired) {
            tvValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isOnlyOneNodeExpanded()) {
                        tView.toggleNode(node, isOnlyOneNodeExpanded());

                    } else {
                        tView.toggleNode(node, isOnlyOneNodeExpanded());
                    }
                }
            });

        }

        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
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

        if (confidentialState == 2 || confidentialState == 3 || confidentialState == 4) {
            icon_lock.setVisibility(View.VISIBLE);
            if (confidentialState == 4) {
                icon_lock.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unlock));
            }
        }

        if(isChecked){
            nodeSelector.setChecked(isChecked);
        }

        return view;
    }


    @Override
    public void toggle(boolean active) {
        //  arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
        if (!mNode.isLeaf())
            arrowView.setImageDrawable(context.getResources().getDrawable(active ? R.drawable.ic_tree_folder_open : R.drawable.ic_tree_close_folder));

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

    public boolean isOnlyOneNodeExpanded() {
        return isOnlyOneNodeExpanded;
    }

    public void setOnlyOneNodeExpanded(boolean expandedOrCollapsed) {
        isOnlyOneNodeExpanded = expandedOrCollapsed;
    }

    public void setTreeLabelBold(boolean treeLabelBold) {
        isTreeLabelBold = treeLabelBold;
    }


    public interface ArrowExpandSelectableHeaderHolderLockIconClickListener {
        void onLockIconClick(Object value, boolean isLeaf);
    }

}
