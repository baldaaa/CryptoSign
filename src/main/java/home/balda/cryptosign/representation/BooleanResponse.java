package home.balda.cryptosign.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Boolean Response")
public class BooleanResponse {

    public BooleanResponse(boolean verified){
        this.verified = verified;
    }
    @ApiModelProperty(notes = "Is verified")
    public Boolean verified;

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
