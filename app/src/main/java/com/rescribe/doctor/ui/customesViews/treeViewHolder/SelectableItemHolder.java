package com.rescribe.doctor.dms.views.treeViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/15/15.
 */
public class SelectableItemHolder extends TreeNode.BaseNodeViewHolder<String> {
    private int leftPadding;
    private TextView tvValue;
    private CheckBox nodeSelector;
    private LinearLayout mainContentLayout;

    public SelectableItemHolder(Context context) {
        this(context, (int) (context.getResources().getDimension(R.dimen.dp30) / context.getResources().getDisplayMetrics().density));
    }

    public SelectableItemHolder(Context context, int leftPadding) {
        super(context);
        this.leftPadding = leftPadding;
    }

    @Override
    public View createNodeView(final TreeNode node, String value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_layout_selectable_item, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        mainContentLayout = (LinearLayout) view.findViewById(R.id.mainContentLayout);
        mainContentLayout.setPadding(leftPadding, 0, 0, 0);

        if (value.contains("|")) {
            tvValue.setText(value.split("\\|")[0]);
        } else {
            tvValue.setText(value);
        }


        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
            }
        });
        nodeSelector.setChecked(node.isSelected());

        return view;
    }


    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        nodeSelector.setChecked(mNode.isSelected());
    }
}
