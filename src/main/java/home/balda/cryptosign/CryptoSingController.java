package home.balda.cryptosign;

import home.balda.cryptosign.representation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.Set;

@Api(value="cryptoSign")
@RestController
@RequestMapping(path = "/crypto/keys")
public class CryptoSingController {

    @Autowired
    CryptoSignService cryptoSignService;

    @ApiOperation(value = "Get all existing key's ids")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<String>> getKeyIds() {
        return new ResponseEntity<Set<String>>(this.cryptoSignService.getKeyIds(), HttpStatus.OK);
    }

    @ApiOperation(value = "Generate new key")
    @RequestMapping(method = RequestMethod.POST, path = "")
    public ResponseEntity<IdResponse> generateKey() {
        return new ResponseEntity<IdResponse>(new IdResponse(this.cryptoSignService.generateKey()), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete key by key Id")
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void deleteKey(@ApiParam(value = "Key unique Id") @PathVariable(name = "id") String id) {
        Objects.requireNonNull(id,"Bad id");
        this.cryptoSignService.deleteKey(id);
    }

    @ApiOperation(value = "Sign on the giving data using the giving key")
    @RequestMapping(method = RequestMethod.POST, path = "/{id}/sign")
    public ResponseEntity<SignatureResponse> signData(@ApiParam(value = "Key unique Id") @PathVariable(name = "id") String id,
                                                      @RequestBody SignDataRequest data){
        Objects.requireNonNull(id,"Bad id");
        Objects.requireNonNull(data.getData(),"Bad data");
        return new ResponseEntity<SignatureResponse>(this.cryptoSignService.singData(id, data), HttpStatus.OK);
    }

    @ApiOperation(value = "Verify the giving signature of the giving data using a giving key")
    @RequestMapping(method = RequestMethod.POST, path = "/{id}/verify")
    public ResponseEntity<BooleanResponse> verifyData(@ApiParam(value = "Key unique Id") @PathVariable(name = "id") String id,
                                                      @RequestBody VerifyDataRequest data){
        Objects.requireNonNull(id,"Bad id");
        Objects.requireNonNull(data.getData(),"Bad data");
        Objects.requireNonNull(data.getSignature(),"Bad signature");
        return new ResponseEntity<BooleanResponse>(new BooleanResponse(this.cryptoSignService.verifyData(id, data)), HttpStatus.OK);
    }
}
