package home.balda.cryptosign;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class KeyManagerTest {

    KeyManager keyManager;

    @Before
    public void setUp() throws Exception {
       keyManager = new KeyManager();
    }
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Test
    public void getKeyIds() {
        String id1 = keyManager.generateKey();
        String id2 = keyManager.generateKey();
        String id3 = keyManager.generateKey();
        Assert.assertTrue(keyManager.getKeyIds().contains(id1));
        Assert.assertTrue(keyManager.getKeyIds().contains(id2));
        Assert.assertTrue(keyManager.getKeyIds().contains(id3));

    }


    @Test
    public void generateKey() {
        String id = keyManager.generateKey();
        Assert.assertEquals(keyManager.getKeyIds().size(), 1);
        Assert.assertTrue(keyManager.getKeyIds().contains(id));
    }

    @Test
    public void deleteKey() {
        String id = keyManager.generateKey();
        String id2 = keyManager.generateKey();
        Assert.assertEquals(keyManager.getKeyIds().size(), 2);
        keyManager.deleteKey(id);
        Assert.assertFalse(keyManager.getKeyIds().contains(id));
        Assert.assertEquals(keyManager.getKeyIds().size(), 1);
    }

    @Test
    public void deleteKey_Fail() {
        exceptionRule.expect(NullPointerException.class);
        keyManager.deleteKey(null);
    }
}