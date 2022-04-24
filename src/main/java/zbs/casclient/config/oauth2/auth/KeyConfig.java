package zbs.casclient.config.oauth2.auth;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * 
 * keytool -genkey -alias oauth-jwt -keyalg RSA -keysize 2048 -keystore .keystore-oauth2-jwt -validity 365 -keypass 123456 -storepass 123456
 *      -alias oauth-jwt：别名=oauth-jwt
 *      -keystore .keystore-oauth2-jwt：文件名称=.keystore-oauth2-jwt
 *      -validity 365：有效期365天
 *      -keypass，-storepass：密码
 * 上述命令会提示迁移到行业标准格式 PKCS12，这是因为jks是java标准，pkcs是通用标准。该命令会生成一个.old忘记做备份：
 *       keytool -importkeystore -srckeystore .keystore-oauth2-jwt -destkeystore .keystore-oauth2-jwt -deststoretype pkcs12
 *       
 * 参考证书信息
 *      keytool -list -v -storepass 123456 -keystore .keystore-oauth2-jwt
 * @author zbs
 * @since 2022/4/2 18:51
 */
public class KeyConfig {
    private static final String KEY_STORE_FILE = "rsa/.keystore-oauth2-jwt";
    private static final String KEY_STORE_PASSWORD = "123456";
    private static final String KEY_ALIAS = "oauth-jwt";
    private static final KeyStoreKeyFactory KEY_STORE_KEY_FACTORY = new KeyStoreKeyFactory(
            new ClassPathResource(KEY_STORE_FILE), KEY_STORE_PASSWORD.toCharArray());
    static final String VERIFIER_KEY_ID = new String(Base64.encode(KeyGenerators.secureRandom(32).generateKey()));

    static RSAPublicKey getVerifierKey() {
        return (RSAPublicKey) getKeyPair().getPublic();
    }

    static RSAPrivateKey getSignerKey() {
        return (RSAPrivateKey) getKeyPair().getPrivate();
    }

    private static KeyPair getKeyPair() {
        return KEY_STORE_KEY_FACTORY.getKeyPair(KEY_ALIAS);
    }

    public static void main(String[] args) {
        KeyStoreKeyFactory k1 = new KeyStoreKeyFactory(
                new ClassPathResource("rsa/.keystore-oauth2-jwt"), "123456".toCharArray());
        KeyPair keyPair = k1.getKeyPair("oauth-jwt");
        System.out.println("public-----------------------------------");
        System.out.println(keyPair.getPublic());
        System.out.println("private-----------------------------------");
        System.out.println(keyPair.getPrivate());
    }
}
