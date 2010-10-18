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

import com.googlecode.jcompare.ui.JCompareWindow;
import com.googlecode.jcompare.model.Item;
import com.googlecode.jcompare.model.impl.ItemImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Tushar Joshi <tusharvjoshi@gmail.com>
 */
public class JCompare {

    public static void compareFileItems(Item item) {

        assert item != null;

        File leftFile = new File(item.getLeftPath() + File.separator +
                item.getName());
        File rightFile = new File(item.getRightPath() + File.separator +
                item.getName());


        if (leftFile.exists() || rightFile.exists()) {
            if (!leftFile.exists()) {
                item.setLeftState(Item.STATE_NOTAVAILABLE);
                item.setRightState(Item.STATE_ORPHAN);
            } else if (!rightFile.exists()) {
                item.setLeftState(Item.STATE_ORPHAN);
                item.setRightState(Item.STATE_NOTAVAILABLE);
            } else {
                if (item.isLeaf()) {
                    long leftLastModified = leftFile.lastModified();
                    long rightLastModified = rightFile.lastModified();

                    if (leftLastModified == rightLastModified) {
                        item.setLeftState(Item.STATE_SAME);
                        item.setRightState(Item.STATE_SAME);
                    } else if (leftLastModified > rightLastModified) {
                        item.setLeftState(Item.STATE_NEW);
                        item.setRightState(Item.STATE_OLD);
                    } else {
                        item.setLeftState(Item.STATE_OLD);
                        item.setRightState(Item.STATE_NEW);
                    }
                } else {
                    item.setLeftState(Item.STATE_SAME);
                    item.setRightState(Item.STATE_SAME);
                }
            }
        }
    }

    /**
     * Check the sub folders of both the paths and create
     * child items with UNCHECKED state
     * @param item
     */
    public static void populateFolderItems(Item item) {

        File leftFile = new File(getFileName(item.getLeftPath(),
                item.getName()));
        File rightFile = new File(getFileName(item.getRightPath(),
                item.getName()));

        Set<String> set = new HashSet<String>();

        String[] fileNames = leftFile.list();
        if (fileNames != null) {
            for (String fileName : fileNames) {
                set.add(fileName);
            }
        }
        fileNames = rightFile.list();
        if (fileNames != null) {
            for (String fileName : fileNames) {
                set.add(fileName);
            }
        }

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        for (String fileName : list) {
            Item child = new ItemImpl();
            child.setName(fileName);
            child.setLeftPath(getFileName(item.getLeftPath(),
                    item.getName()));
            child.setRightPath(getFileName(item.getRightPath(),
                    item.getName()));
            item.addChild(child);
        }

        differentiateFolderLeafs(item);

        List<Item> leafList = getLeafList(item);
        List<Item> folderList = getNonLeafList(item);

        item.clearChildren();
        for (Item child : folderList) {
            item.addChild(child);
        }
        for (Item child : leafList) {
            item.addChild(child);
        }

    }

    public static String getFileName(String path, String name) {
        if (name.length() == 0) {
            return path;
        } else {
            return path + File.separator + name;
        }
    }

    public static void setLeaf(Item item) {
        File leftFile = new File(getFileName(item.getLeftPath(),
                item.getName()));
        File rightFile = new File(getFileName(item.getRightPath(),
                item.getName()));

        if (leftFile.isDirectory() || rightFile.isDirectory()) {
            item.setLeaf(false);
        } else {
            item.setLeaf(true);
        }
    }

    public static void differentiateFolderLeafs(Item item) {
        int count = item.getChildCount();
        for (int index = 0; index < count; index++) {
            Item child = item.getChild(index);
            setLeaf(child);
        }
    }

    public static List<Item> getLeafList(Item item) {
        List<Item> list = new ArrayList<Item>();
        int count = item.getChildCount();
        for (int index = 0; index < count; index++) {
            Item child = item.getChild(index);
            if (child.isLeaf()) {
                list.add(child);
            }
        }
        return list;
    }

    public static List<Item> getNonLeafList(Item item) {
        List<Item> list = new ArrayList<Item>();
        int count = item.getChildCount();
        for (int index = 0; index < count; index++) {
            Item child = item.getChild(index);
            if (!child.isLeaf()) {
                list.add(child);
            }
        }
        return list;
    }

    public static void compare(Item item) {

        populateFolderItems(item);

        List<Item> leafList = getLeafList(item);
        for (Item child : leafList) {
            compareFileItems(child);
        }

        List<Item> folderList = getNonLeafList(item);
        if (folderList.size() > 0) {
            for (Item child : folderList) {
                compare(child);
            }
        }

        if (leafList.size() == 0 && folderList.size() == 0) {
            compareFileItems(item);
        } else {

            // time to set status of
            // this item
            compareFileItems(item);

            if (item.getLeftState() != Item.STATE_NOTAVAILABLE &&
                    item.getRightState() != Item.STATE_NOTAVAILABLE) {

                int count = item.getChildCount();
                int leftOrphanCount = 0;
                int leftNewCount = 0;
                int leftOldCount = 0;
                int rightOrphanCount = 0;
                int rightNewCount = 0;
                int rightOldCount = 0;
                for (int index = 0; index < count; index++) {
                    Item child = item.getChild(index);
                    int leftState = child.getLeftState();
                    switch (leftState) {
                        case Item.STATE_NEW:
                            leftNewCount++;
                            break;
                        case Item.STATE_OLD:
                            leftOldCount++;
                            break;
                        case Item.STATE_ORPHAN:
                            leftOrphanCount++;
                            break;
                    }
                    int rightState = child.getRightState();
                    switch (rightState) {
                        case Item.STATE_NEW:
                            rightNewCount++;
                            break;
                        case Item.STATE_OLD:
                            rightOldCount++;
                            break;
                        case Item.STATE_ORPHAN:
                            rightOrphanCount++;
                            break;
                    }
                }

                if (leftNewCount > 0 || leftOldCount > 0 || leftOrphanCount > 0) {
                    if (leftNewCount > 0 || leftOrphanCount > 0) {
                        if( leftNewCount > 0 && leftOrphanCount > 0) {
                            item.setLeftState(item.STATE_NEWOLD);
                        } else {
                            item.setLeftState(item.STATE_NEW);
                        }
                    } else {
                        item.setLeftState(item.STATE_OLD);
                    }
                }

                if (rightNewCount > 0 || rightOldCount > 0 || rightOrphanCount > 0) {
                    if (rightNewCount > 0 || rightOrphanCount > 0) {
                        if( rightNewCount > 0 && rightOrphanCount > 0) {
                            item.setRightState(item.STATE_NEWOLD);
                        } else {
                            item.setRightState(item.STATE_NEW);
                        }
                    } else {
                        item.setRightState(item.STATE_OLD);
                    }
                }

                if( item.getLeftState() == Item.STATE_NEW) {
                    item.setRightState(Item.STATE_OLD);
                }

                if( item.getRightState() == Item.STATE_NEW) {
                    item.setLeftState(Item.STATE_OLD);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JCompare.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(JCompare.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(JCompare.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JCompare.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JCompareWindow window = new JCompareWindow();
                window.setVisible(true);
                window.setPaths("e:\\test1","e:\\test2");
            }
        });
    }
}
