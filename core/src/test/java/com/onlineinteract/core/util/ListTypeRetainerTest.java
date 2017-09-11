package com.onlineinteract.core.util;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.Color;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListTypeRetainerTest {


    @Test
    public void shouldReturnNewListContainingSubtypePassedInTest() {

        List<WorkbenchItem> workbenchItems = new ArrayList<WorkbenchItem>();
        workbenchItems.add(new WorkbenchOutline(11, 11, null, null));
        workbenchItems.add((new Template(null, null, null, null, 22, Color.FOREST, Color.FOREST, "µicroservice")));
        workbenchItems.add((new Template(null, null, null, null, 22, Color.FOREST, Color.FOREST, "Infrastructure")));
        workbenchItems.add((new Template(null, null, null, null, 22, Color.FOREST, Color.FOREST, "Scripts")));

        List<Template> retainedList = ListTypeRetainer.<WorkbenchItem, Template>retainedList(workbenchItems, Template.class);

        assertEquals(3, retainedList.size());
    }

}
