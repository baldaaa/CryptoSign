package home.balda.cryptosign;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.security.*;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Singleton Class holds generated key pairs
 */
@Component
@Scope("singleton")
public class KeyManager{
    Map<String, KeyPair> keysHolder;
    KeyPairGenerator keyPairGen;

    public KeyManager(){
        keysHolder = new ConcurrentHashMap<>();

        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public Set<String> getKeyIds() {
        return keysHolder.keySet();
    }


    public String generateKey() {
        String id  = UUID.randomUUID().toString();
        keysHolder.put(id, keyPairGen.generateKeyPair());
        return id;
    }


    public void deleteKey( String id) {
        KeyPair keyPair = keysHolder.remove(id);
        Objects.requireNonNull(keyPair, "Key does not exist");
    }


    public PublicKey getPublicKey(String id) {
        KeyPair keyPair = keysHolder.get(id);
        Objects.requireNonNull(keyPair, "Key does not exist");
        return keyPair.getPublic();
    }

    public PrivateKey getPrivateKey(String id) {
        KeyPair keyPair = keysHolder.get(id);
        Objects.requireNonNull(keyPair, "Key does not exist");
        return keyPair.getPrivate();
    }
}
