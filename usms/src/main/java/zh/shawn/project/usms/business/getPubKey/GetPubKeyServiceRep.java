package zh.shawn.project.usms.business.getPubKey;

import zh.shawn.project.framework.commons.service.core.CommonServiceResponseData;

public class GetPubKeyServiceRep extends CommonServiceResponseData {

    private String publicKey; // 公钥

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
