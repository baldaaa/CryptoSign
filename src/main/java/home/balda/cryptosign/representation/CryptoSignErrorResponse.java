package home.balda.cryptosign.representation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Error Response")
public class CryptoSignErrorResponse {
    public CryptoSignErrorResponse(String timestamp, String error, String errorMessage) {
        this.timestamp = timestamp;
        this.error = error;
        this.errorMessage = errorMessage;
    }

    @ApiModelProperty(notes = "Timestamp")
    public String timestamp;
    @ApiModelProperty(notes = "Error")
    public String error;
    @ApiModelProperty(notes = "Error message")
    public String errorMessage;

}
