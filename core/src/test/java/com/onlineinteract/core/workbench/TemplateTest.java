package com.onlineinteract.core.workbench;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TemplateTest {

	private static final String COMMAND_CONTAINING_ENV_VARIABLE_1 = "java -jar %WORK%\\stateful-list-service\\target\\stateful-list-service-0.0.1-SNAPSHOT.jar";
	private static final String RESULT_1 = "java -jar C:\\work\\stateful-list-service\\target\\stateful-list-service-0.0.1-SNAPSHOT.jar";
	private static final String COMMAND_CONTAINING_ENV_VARIABLE_2 = "%WORK% this is a tandom %WORK% test to see if all envs get replaced %WORK%";
	private static final String RESULT_2 = "C:\\work this is a tandom C:\\work test to see if all envs get replaced C:\\work";
	private static final String COMMAND_NO_ENV_VAR = "java --version";
	
	Template uut;

	@Before
	public void setup() {
		uut = new Template();
	}

	@Test
	public void replaceEnvVars1() {
		String command = uut.replaceEnvVars(COMMAND_CONTAINING_ENV_VARIABLE_1);
		assertEquals(RESULT_1, command);
	}

	@Test
	public void replaceEnvVars2() {
		String command = uut.replaceEnvVars(COMMAND_CONTAINING_ENV_VARIABLE_2);
		assertEquals(RESULT_2, command);
	}

	@Test
	public void ensuresNoCommandsAreReplacedWithNoEnvVarsSupplied() {
		String command = uut.replaceEnvVars(COMMAND_NO_ENV_VAR);
		assertEquals(COMMAND_NO_ENV_VAR, command);
	}
	
}
