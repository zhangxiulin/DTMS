package zh.shawn.project.usms.business.getPubKey;

import org.apache.log4j.Logger;
import zh.shawn.project.framework.boot.utils.RSAUtils2;
import zh.shawn.project.framework.commons.exception.ServiceBusinessException;
import zh.shawn.project.framework.commons.service.core.CommonBusinessService;


public class GetPubKeyService extends CommonBusinessService<GetPubKeyServiceReq, GetPubKeyServiceRep, GetPubKeyServiceSessionData> {

    private Logger LOGGER = Logger.getLogger(GetPubKeyService.class);
    private GetPubKeyServiceSessionData session;

    @Override
    public String getTargetTemplateId() {
        return "";
    }

    @Override
    public GetPubKeyServiceReq getRequestInstance() {
        return new GetPubKeyServiceReq();
    }

    @Override
    public GetPubKeyServiceRep getResponseInstance() {
        return new GetPubKeyServiceRep();
    }

    @Override
    public GetPubKeyServiceRep service(GetPubKeyServiceReq req) throws ServiceBusinessException {
        GetPubKeyServiceRep rep = new GetPubKeyServiceRep();

        // 公钥、密钥
        String publicKey = null;
        String privateKey = null;

        try {
            RSAUtils2 rSAUtils = new RSAUtils2();
            rSAUtils.createKeyPair();
            privateKey = rSAUtils.getPrik();
            publicKey = rSAUtils.getPubk();
        } catch (Exception e) {
            LOGGER.error("系统错误！"+e.getMessage(), e);
            throw new ServiceBusinessException("登录失败，系统错误！");
        }

        session.setPublicKey(publicKey);
        session.setPrivateKey(privateKey);

        rep.setPublicKey(publicKey);

        return rep;
    }

    @Override
    public GetPubKeyServiceSessionData getSessionInstance() {
        return new GetPubKeyServiceSessionData();
    }

    @Override
    public GetPubKeyServiceSessionData getSession() {
        return this.session;
    }

    @Override
    public void updateSession(GetPubKeyServiceSessionData session) {
        this.session = session;
    }
}
