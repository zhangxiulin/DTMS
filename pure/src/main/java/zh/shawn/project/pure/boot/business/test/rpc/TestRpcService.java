package zh.shawn.project.pure.boot.business.test.rpc;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.boot.rpc.RpcAccessLayer;
import zh.shawn.project.pure.boot.service.contract.IRpcService;
import zh.shawn.project.pure.commons.exception.ServiceBusinessException;
import zh.shawn.project.pure.commons.service.core.CommonBusinessService;

import java.util.HashMap;
import java.util.Map;

public class TestRpcService extends CommonBusinessService<TestRpcServiceReq, TestRpcServiceRep, TestRpcServiceSessionData> {

	private Logger log = LoggerFactory.getLogger(TestRpcService.class);
	TestRpcServiceSessionData sd;

	@Override
	public String getTargetTemplateId() {
		return "test.ftl";
	}

	@Override
	public TestRpcServiceReq getRequestInstance() {
		return new TestRpcServiceReq();
	}

	@Override
	public TestRpcServiceRep getResponseInstance() {
		return new TestRpcServiceRep();
	}

	@Override
	public TestRpcServiceSessionData getSessionInstance() {
		return new TestRpcServiceSessionData();
	}

	@Override
	public TestRpcServiceRep service(TestRpcServiceReq req) throws ServiceBusinessException {
		log.debug("这是一个测试服务");
		log.debug("入参:" + JSON.toJSONString(req));
		TestRpcServiceRep rep = this.getResponseInstance();

		IRpcService rpcService = (IRpcService) RpcAccessLayer.getRpc("dubbo").get("camsRpcService");
		Map<String, Object> rpcRequest = new HashMap<>();
		rpcRequest.put("f", "test2");
		rpcRequest.put("foo", "a");
		rpcRequest.put("session_provider", "infinispan_based");
		rpcRequest.put("seesion_token", "");
		rpcRequest.put("enc", "{\"name\":\"zhangsan\"}");

		Object rpcResponse = rpcService.service(rpcRequest);

		log.debug(rpcResponse.toString());

		rep.setO("ddddd");
		log.debug("出参:" + JSON.toJSONString(rep));
		return rep;
	}

	@Override
	public TestRpcServiceSessionData getSession() {
		return this.sd;
	}

	@Override
	public void updateSession(TestRpcServiceSessionData arg0) {
		this.sd = arg0;
	}

}
