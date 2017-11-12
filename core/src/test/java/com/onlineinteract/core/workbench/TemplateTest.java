package com.onlineinteract.core.workbench;

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineinteract.core.type.ProcessingType;
import com.onlineinteract.core.type.TemplateType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TemplateTest {

	private static final String COMMAND_CONTAINING_ENV_VARIABLE_1 = "java -jar %WORK%\\stateful-list-service\\target\\stateful-list-service-0.0.1-SNAPSHOT.jar";
	private static final String RESULT_1 = "java -jar C:\\work\\stateful-list-service\\target\\stateful-list-service-0.0.1-SNAPSHOT.jar";
	private static final String COMMAND_CONTAINING_ENV_VARIABLE_2 = "%WORK% this is a tandom %WORK% test to see if all envs get replaced %WORK%";
	private static final String COMMAND_CONTAINING_ENV_VARIABLE_3 = "cmd /c wmic process where (commandline like \"%%elastic%%\" and not name=\"wmic.exe\") delete";
	private static final String RESULT_2 = "C:\\work this is a tandom C:\\work test to see if all envs get replaced C:\\work";
	private static final String COMMAND_NO_ENV_VAR = "java --version";

	Template uut;

	@Before
	public void setup() {
		uut = new Template();
	}

	/**
	 * Note: enable the following two tests once %WORK% environment
	 * variable has been set.
	 */

	@Ignore
	@Test
	public void replaceEnvVars1() {
		String command = uut.replaceEnvVars(COMMAND_CONTAINING_ENV_VARIABLE_1);
		assertEquals(RESULT_1, command);
	}

	@Ignore
	@Test
	public void replaceEnvVars2() {
		String command = uut.replaceEnvVars(COMMAND_CONTAINING_ENV_VARIABLE_2);
		assertEquals(RESULT_2, command);
	}

	@Test
	public void replaceEnvVars3() {
		String command = uut.replaceEnvVars(COMMAND_CONTAINING_ENV_VARIABLE_3);
		assertEquals(COMMAND_CONTAINING_ENV_VARIABLE_3, command);
	}

	@Test
	public void ensuresNoCommandsAreReplacedWithNoEnvVarsSupplied() {
		String command = uut.replaceEnvVars(COMMAND_NO_ENV_VAR);
		assertEquals(COMMAND_NO_ENV_VAR, command);
	}

	@Test
	public void convertToJson() throws IOException {

		// Object to JSON string
		ObjectMapper mapper = new ObjectMapper();
		Template template = new Template();
		template.setX(100.00f);
		template.setY(100.00f);
		template.setColor1(Color.FOREST);
		template.setColor2(Color.CHARTREUSE);
		template.setLabel("Test Service");
		template.setType(TemplateType.MICROSERVICE);
		template.setProcessingType(ProcessingType.SEQ);
		template.setUuid(UUID.randomUUID());
		String templateString = mapper.writeValueAsString(template);
		System.out.println(templateString);

		// Json String to object
		Template returnTemplate = mapper.readValue(templateString, Template.class);
		System.out.println(returnTemplate.getX());
		System.out.println(returnTemplate.getUuid());
		System.out.println(returnTemplate.getColor1().toString());

		// Write json entries out to file
		templateString += "\n";
		File file = new File("c:\\tmp\\test-topology");
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(templateString.getBytes());
		fos.write(templateString.getBytes());
		fos.write(templateString.getBytes());
		fos.flush();
		fos.close();

		// Read json entries in from file
		String readLine = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((readLine = br.readLine()) != null) {
			System.out.println(readLine);
		}
		br.close();

	}
}
