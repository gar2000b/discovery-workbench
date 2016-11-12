package com.onlineinteract.core.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchOutline;
import com.onlineinteract.core.workbench.WorkbenchRenderer;

public class ListTypeRetainerTest {

	@Test
	public void shouldReturnNewListContainingSubtypePassedInTest() {

		List<WorkbenchRenderer> workbenchItems = new ArrayList<WorkbenchRenderer>();
		workbenchItems.add(new WorkbenchOutline(11, 11, null, null));
		workbenchItems.add(new Template());
		workbenchItems.add(new Template());
		workbenchItems.add(new Template());

		List<Template> retainedList = ListTypeRetainer.<WorkbenchRenderer, Template>retainedList(workbenchItems,
				Template.class);

		assertEquals(3, retainedList.size());
	}

}
