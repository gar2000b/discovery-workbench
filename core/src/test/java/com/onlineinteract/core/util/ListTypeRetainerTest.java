package com.onlineinteract.core.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;

public class ListTypeRetainerTest {

	@Test
	public void shouldReturnNewListContainingSubtypePassedInTest() {

		List<WorkbenchItem> workbenchItems = new ArrayList<WorkbenchItem>();
		workbenchItems.add(new WorkbenchOutline(11, 11, null, null));
		workbenchItems.add(new Template());
		workbenchItems.add(new Template());
		workbenchItems.add(new Template());

		List<Template> retainedList = ListTypeRetainer.<WorkbenchItem, Template>retainedList(workbenchItems,
				Template.class);

		assertEquals(3, retainedList.size());
	}

}
