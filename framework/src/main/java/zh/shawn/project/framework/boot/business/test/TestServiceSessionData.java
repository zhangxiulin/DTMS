package zh.shawn.project.framework.boot.business.test;

import zh.shawn.project.framework.commons.service.core.CommonSessionData;

public class TestServiceSessionData  extends CommonSessionData {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
