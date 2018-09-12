package com.scorg.dms.ui.customesViews.treeViewHolder;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.scorg.dms.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class SelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private int leftPadding;
    private boolean isDefaultExpanded;
    private TextView tvValue;
    private LinearLayout mainContentLayout;
    private PrintView arrowView;
    private AppCompatCheckBox nodeSelector;

    public SelectableHeaderHolder(Context context, boolean isDefaultExpanded) {
        this(context, isDefaultExpanded, (int) (context.getResources().getDimension(R.dimen.dp10) / context.getResources().getDisplayMetrics().density));
    }

    public SelectableHeaderHolder(Context context, boolean isDefaultExpanded, int leftPadding) {
        super(context);
        this.leftPadding = leftPadding;
        this.isDefaultExpanded = isDefaultExpanded;
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItemHolder.IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_layout_selectable_header, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        mainContentLayout = (LinearLayout) view.findViewById(R.id.mainContentLayout);
        mainContentLayout.setPadding(leftPadding, 0, 0, 0);

        if (value.text.contains("|")) {
            tvValue.setText(value.text.split("\\|")[0]);
        } else {
            tvValue.setText(value.text);
        }

        nodeSelector = (AppCompatCheckBox) view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, isChecked);
                }
            }
        });
        nodeSelector.setChecked(node.isSelected());

        node.setExpanded(isDefaultExpanded);

        return view;
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        nodeSelector.setChecked(mNode.isSelected());
    }
}
