package home.balda.cryptosign.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Data Signature Response")
public class SignatureResponse {

    public SignatureResponse(String signature){
        this.signature = signature;
    }

    @ApiModelProperty(notes = "Signature")
    String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
