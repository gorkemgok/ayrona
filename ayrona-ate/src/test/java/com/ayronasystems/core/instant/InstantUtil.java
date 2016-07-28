package com.ayronasystems.core.instant;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by gorkemgok on 25/07/16.
 */
public class InstantUtil {

    public static void copyToClipboard(String text){
        StringSelection stringSelection = new StringSelection (text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

}
