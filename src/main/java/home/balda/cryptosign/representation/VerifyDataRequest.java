package home.balda.cryptosign.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Verify Data Request")
public class VerifyDataRequest extends SignDataRequest{

    @ApiModelProperty(notes = "Signature")
    String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
