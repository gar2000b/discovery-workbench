package com.onlineinteract.core.util;

import java.util.ArrayList;
import java.util.List;

public class ListTypeRetainer {

    /**
     * This method passes through a List retaining elements of a specific type passed in while stripping out all others.
     * 
     * Returns a new retained list.
     */
    @SuppressWarnings("unchecked")
    public static <T, S> List<S> retainedList(List<T> list, Class<S> retainedType) {
        List<S> returnList = new ArrayList<S>();

        for (T item : list) {
            if (retainedType.equals(item.getClass())) {
                returnList.add((S) item);
            }
        }

        return returnList;
    }
}
