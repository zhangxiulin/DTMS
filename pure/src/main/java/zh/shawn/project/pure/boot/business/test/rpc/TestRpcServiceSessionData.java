package zh.shawn.project.pure.boot.business.test.rpc;

import zh.shawn.project.pure.commons.service.core.CommonSessionData;

public class TestRpcServiceSessionData extends CommonSessionData {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
