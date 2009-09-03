/*
 * The MIT License
 * 
 * Copyright (c) <year> <copyright holders>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.jcompare;

import com.googlecode.jcompare.model.Item;
import com.googlecode.jcompare.resources.IconUtils;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class MyCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf,
            int row, boolean hasFocus) {
        DefaultTreeCellRenderer component =
                (DefaultTreeCellRenderer) super.getTreeCellRendererComponent(
                tree, value, selected, expanded, leaf, row, hasFocus);
        if (value instanceof Item) {
            Item item = (Item) value;
            int state;
            if (item instanceof LeftItem) {
                state = item.getLeftState();
            } else {
                state = item.getRightState();
            }
            String iconName = "no-folder.gif";
            Color textColor = component.getForeground();
            Color bgSelectionColor = component.getBackgroundSelectionColor();
            switch (state) {
                case Item.STATE_NEW:
                    iconName = "folder-new.gif";
                    textColor = Color.red;
                    break;
                case Item.STATE_OLD:
                    iconName = "folder-same.gif";
                    textColor = Color.gray;
                    break;
                case Item.STATE_ORPHAN:
                    iconName = "folder-orphan.gif";
                    textColor = Color.blue;
                    break;
                case Item.STATE_SAME:
                    iconName = "folder-same.gif";
                    textColor = Color.gray;
                    break;
                case Item.STATE_NEWOLD:
                    iconName = "folder-newold.gif";
                    textColor = Color.blue;
                    break;
                case Item.STATE_NOTAVAILABLE:
                    iconName = "no-folder.gif";
                    textColor = Color.white;
                    bgSelectionColor = Color.white;
                    break;
            }
            Icon icon = null;
            if( iconName != null ) {
                icon = IconUtils.getIcon(iconName);
            }
            component.setIcon(icon);
            component.setForeground(textColor);
            component.setBackgroundSelectionColor(bgSelectionColor);
        }
        return component;
    }
}
