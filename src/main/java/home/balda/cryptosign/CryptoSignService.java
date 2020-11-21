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

    /**
     *
     * @return set of all key pairs
     */
    public Set<String> getKeyIds() {
        return keyManager.getKeyIds();
    }

    /**
     * generates new key pair
     * @return unique id of generated key pair
     */
    public String generateKey() {
        return keyManager.generateKey();
    }

    /**
     * delete key pair by given id
     * @param id
     */
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

        return keyManager.singData(id, data);
    }

    /**
     * Verify the signature for given data using public key
     * @param id - generated key pair id
     * @param data - signed data and signature
     * @return - true/false verification result
     */
    public Boolean verifyData(String id, VerifyDataRequest data) {
        return keyManager.verifyData(id,data);
    }
}
