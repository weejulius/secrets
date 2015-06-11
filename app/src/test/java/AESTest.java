import junit.framework.Assert;

import org.junit.Test;

import jyu.secret.Encrypts;

/**
 * Created by jyu on 15-6-3.
 */

/**
 * @author carlos carlosk@163.com
 * @version 创建时间：2012-5-17 上午9:48:35 类说明
 */

public class AESTest {
    public static final String TAG = "AESUtils";

    private Encrypts encrypts = Encrypts.ins();


    @Test
    public void test1() {

        Assert.assertEquals("E6B71C52A813E30B76D75D491B0204D1", encrypts.encodeByAes("12313213123123", "12312312"));
        Assert.assertEquals("12312312", encrypts.decodeByAes("12313213123123", "E6B71C52A813E30B76D75D491B0204D1"));
        Assert.assertEquals("01E8333E06179CF54A819FFD703516A0258CF760E3FF22E3D9A16A71225AAA3AC97EF0CDE22E3EE3D2BD13692110076D", encrypts.encodeByAes("12313213123123", "E6B71C52A813E30B76D75D491B0204D1"));
        Assert.assertEquals("E6B71C52A813E30B76D75D491B0204D1", encrypts.decodeByAes("12313213123123", "01E8333E06179CF54A819FFD703516A0258CF760E3FF22E3D9A16A71225AAA3AC97EF0CDE22E3EE3D2BD13692110076D"));

        encrypts.loadRandomKey(encrypts.genRandomKey());
        encrypts.genSeedForShortPwd("cscs");

        String encrypt = encrypts.encryptSecretData("ab");

        System.out.println(encrypt);

        Assert.assertEquals("ab", encrypts.decryptSecretData(encrypt));

        encrypt = encrypts.encryptSecretPwd("abcd","cscscscscscs");

        System.out.println(encrypt);

        Assert.assertEquals("cscscscscscs", encrypts.decryptSecretPwd("abcd",encrypt));

    }


}
