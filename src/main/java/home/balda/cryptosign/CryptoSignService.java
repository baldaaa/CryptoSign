package home.balda.cryptosign;

import home.balda.cryptosign.representation.SignDataRequest;
import home.balda.cryptosign.representation.SignatureResponse;
import home.balda.cryptosign.representation.VerifyDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Set;

@Service
public class CryptoSignService {

    @Autowired
    KeyManager keyManager;


    public Set<String> getKeyIds() {
        return keyManager.getKeyIds();
    }

    public String generateKey() {
        return keyManager.generateKey();
    }

    public void deleteKey(String id) {
        keyManager.deleteKey(id);
    }


    /**
     * Calculate the Signature for given data using private key
     * @param id - generated key pair id
     * @param data - data to sign
     * @return - calculated signature
     */
    public SignatureResponse singData(String id, SignDataRequest data){

        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(keyManager.getPrivateKey(id));
            sign.update(data.getData().getBytes(StandardCharsets.UTF_8));

            //Create the signature
            byte[] signature = sign.sign();
            //Convert signature byte array to string
            String signatureStr = new String(Base64Utils.encode(signature), StandardCharsets.UTF_8);
            return new SignatureResponse(signatureStr);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }
    }

    /**
     * Verify the signature for given data using public key
     * @param id - generated key pair id
     * @param data - signed data and signature
     * @return - true/false verification result
     */
    public Boolean verifyData(String id, VerifyDataRequest data) {
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            byte[] bytes = data.getData().getBytes(StandardCharsets.UTF_8);
            sign.initVerify(keyManager.getPublicKey(id));
            sign.update(bytes);

            //convert signature back to byte array
            byte[] signature = Base64Utils.decode(data.getSignature().getBytes(StandardCharsets.UTF_8));

            //Verifying the signature
            return sign.verify(signature);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            throw new RuntimeException("Internal server error");
        }catch(SignatureException e){
            e.printStackTrace();
            return false;
        }
    }
}
