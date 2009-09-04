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

package com.googlecode.jcompare.ui;

import com.googlecode.jcompare.*;
import com.googlecode.jcompare.model.Item;
import java.beans.PropertyChangeListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class RightItem implements Item {
    private final Item item;
    public RightItem( Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getStateDescription(int state) {
        return item.getStateDescription(state);
    }

    public int getLeftState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLeftState(int state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRightState() {
        return item.getRightState();
    }

    public void setRightState(int state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        if( item.getRightState() == Item.STATE_NOTAVAILABLE) {
            return item.getName();
        } else {
            return item.getName();
        }
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLeftPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setLeftPath(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRightPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setRightPath(String path) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isLeaf() {
        return item.isLeaf();
    }

    public void setLeaf(boolean value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getChildCount() {
        return item.getChildCount();
    }

    public void clearChildren() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addChild(Item childItem) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Item getChild(int index) {
        return new RightItem(item.getChild(index));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        //
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        //
    }


}

