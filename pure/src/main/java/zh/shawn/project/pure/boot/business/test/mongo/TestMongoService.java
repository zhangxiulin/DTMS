package zh.shawn.project.pure.boot.business.test.mongo;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zh.shawn.project.pure.boot.dal.MongoAccessLayer;
import zh.shawn.project.pure.commons.exception.ServiceBusinessException;
import zh.shawn.project.pure.commons.service.core.CommonBusinessService;

import java.util.Arrays;

public class TestMongoService extends CommonBusinessService<TestMongoServiceReq, TestMongoServiceRep, TestMongoServiceSessionData> {

	private Logger log = LoggerFactory.getLogger(TestMongoService.class);
	TestMongoServiceSessionData sd;

	@Override
	public String getTargetTemplateId() {
		return "test.ftl";
	}

	@Override
	public TestMongoServiceReq getRequestInstance() {
		return new TestMongoServiceReq();
	}

	@Override
	public TestMongoServiceRep getResponseInstance() {
		return new TestMongoServiceRep();
	}

	@Override
	public TestMongoServiceSessionData getSessionInstance() {
		return new TestMongoServiceSessionData();
	}

	@Override
	public TestMongoServiceRep service(TestMongoServiceReq req) throws ServiceBusinessException {
		log.debug("这是一个测试服务");
		log.debug("入参:" + JSON.toJSONString(req));
		TestMongoServiceRep rep = this.getResponseInstance();

		MongoClient mongoClient = MongoAccessLayer.getMongoClient("CT");
		MongoDatabase database = mongoClient.getDatabase("CT");
		MongoCollection<Document> test = database.getCollection("test");
		String[] tags = new String[]{"a","b"};
		Document canvas = new Document("item", "canvas")
				.append("qty", 101)
				.append("tags", Arrays.asList(tags));
		test.insertOne(canvas);

		rep.setO("ddddd");
		log.debug("出参:" + JSON.toJSONString(rep));
		return rep;
	}

	@Override
	public TestMongoServiceSessionData getSession() {
		return this.sd;
	}

	@Override
	public void updateSession(TestMongoServiceSessionData arg0) {
		this.sd = arg0;
	}

}
