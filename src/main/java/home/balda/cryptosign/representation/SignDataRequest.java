package home.balda.cryptosign.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Sign Data Request")
public class SignDataRequest{
    @ApiModelProperty(notes = "Data")
    String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
