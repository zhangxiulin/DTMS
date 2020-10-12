package zh.shawn.project.usms.business.getPubKey;

import zh.shawn.project.pure.commons.service.core.CommonSessionData;

public class GetPubKeyServiceSessionData extends CommonSessionData {

    //公钥、私钥
    private String publicKey;

    private String privateKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
