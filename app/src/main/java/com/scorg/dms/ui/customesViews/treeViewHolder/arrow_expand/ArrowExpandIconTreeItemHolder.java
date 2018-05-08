package com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.scorg.dms.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class ArrowExpandIconTreeItemHolder extends TreeNode.BaseNodeViewHolder<ArrowExpandIconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private PrintView arrowView;

    public ArrowExpandIconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.treeview_layout_icon_node, null, false);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text.toString());

        return view;
    }

    @Override
    public void toggle(boolean active) {
        //  arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    public static class IconTreeItem {
        public int icon;
        public Object text;
        public Object objectData;
        public int level;

        public IconTreeItem(int icon, Object text, Object objectData, int level) {
            this.icon = icon;
            this.text = text;
            this.objectData = objectData;
            this.level = level;
        }

      /*  @Override
        public String toString() {
            return objectData.toString();
        }*/
    }

}
