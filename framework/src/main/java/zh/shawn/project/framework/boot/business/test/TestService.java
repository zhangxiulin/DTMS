package zh.shawn.project.framework.boot.business.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.commons.exception.ServiceBusinessException;
import zh.shawn.project.framework.commons.service.core.CommonBusinessService;

public class TestService extends CommonBusinessService<TestServiceReq, TestServiceRep, TestServiceSessionData> {

	private Logger log = LoggerFactory.getLogger(TestService.class);
	TestServiceSessionData sd;

	@Override
	public String getTargetTemplateId() {
		return "";
	}

	@Override
	public TestServiceReq getRequestInstance() {
		return new TestServiceReq();
	}

	@Override
	public TestServiceRep getResponseInstance() {
		return new TestServiceRep();
	}

	@Override
	public TestServiceSessionData getSessionInstance() {
		return new TestServiceSessionData();
	}

	@Override
	public TestServiceRep service(TestServiceReq arg0) throws ServiceBusinessException {
		log.debug("这是一个测试服务");
		log.debug("会话数据:" + this.getSessionData());
		TestServiceRep rep = this.getResponseInstance();
		return rep;
	}

	@Override
	public TestServiceSessionData getSession() {
		return this.sd;
	}

	@Override
	public void updateSession(TestServiceSessionData arg0) {
		this.sd = arg0;
	}

}
