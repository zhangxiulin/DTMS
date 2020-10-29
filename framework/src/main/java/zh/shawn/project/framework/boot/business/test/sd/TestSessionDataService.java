package zh.shawn.project.framework.boot.business.test.sd;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.framework.commons.exception.ServiceBusinessException;
import zh.shawn.project.framework.commons.service.core.CommonBusinessService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestSessionDataService extends CommonBusinessService<TestSessionDataServiceReq, TestSessionDataServiceRep, TestSessionDataServiceSessionData> {

	private Logger log = LoggerFactory.getLogger(TestSessionDataService.class);
	TestSessionDataServiceSessionData sd;

	@Override
	public String getTargetTemplateId() {
		return "test.ftl";
	}

	@Override
	public TestSessionDataServiceReq getRequestInstance() {
		return new TestSessionDataServiceReq();
	}

	@Override
	public TestSessionDataServiceRep getResponseInstance() {
		return new TestSessionDataServiceRep();
	}

	@Override
	public TestSessionDataServiceSessionData getSessionInstance() {
		return new TestSessionDataServiceSessionData();
	}

	@Override
	public TestSessionDataServiceRep service(TestSessionDataServiceReq req) throws ServiceBusinessException {
		log.debug("这是一个测试服务");
		log.debug("入参:" + JSON.toJSONString(req));
		TestSessionDataServiceRep rep = this.getResponseInstance();
		log.debug("会话数据:" + this.getSessionData());
		log.debug("待变更的会话数据:" + this.sd.getName());
		this.sd.setName(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		log.debug("将变更的会话数据:" + this.sd.getName());
		rep.setO("ddddd");
		log.debug("出参:" + JSON.toJSONString(rep));
		return rep;
	}

	@Override
	public TestSessionDataServiceSessionData getSession() {
		return this.sd;
	}

	@Override
	public void updateSession(TestSessionDataServiceSessionData arg0) {
		this.sd = arg0;
	}

}
