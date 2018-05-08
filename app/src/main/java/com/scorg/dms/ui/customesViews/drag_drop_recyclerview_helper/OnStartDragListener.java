/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scorg.dms.ui.customesViews.drag_drop_recyclerview_helper;

import android.support.v7.widget.RecyclerView;

import com.scorg.dms.model.waiting_list.Active;
import com.scorg.dms.model.waiting_list.ViewAll;

/**
 * Listener for manual initiation of a drag.
 */
public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

    void onDeleteViewAllLayoutClicked(int adapterPosition, ViewAll viewAll);
    void onDeleteActiveLayoutClicked(int adapterPosition, Active active);

}
