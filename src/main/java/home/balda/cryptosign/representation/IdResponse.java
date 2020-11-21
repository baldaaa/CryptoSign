package home.balda.cryptosign.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Key Id Response")
public class IdResponse {

    public IdResponse(String id){
        this.keyId = id;
    }
    @ApiModelProperty(notes = "Key Id")
    String keyId;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
