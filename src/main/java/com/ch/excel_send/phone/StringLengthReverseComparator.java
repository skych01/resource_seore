package com.ch.excel_send.phone;

import java.util.Comparator;

/**
 * @author ch
 */
public class StringLengthReverseComparator implements Comparator<String> {

    @Override
    public int compare(String a, String b) {
        int x = a == null? 0: a.length();
        int y = b == null? 0: b.length();
        return (x < y) ? 1 : ((x == y) ? 0 : -1);
    }
}
