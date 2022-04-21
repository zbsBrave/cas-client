package zbs.casclient.RSA;

import com.nimbusds.jose.util.IOUtils;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author zbs
 * @since 2022/4/20 15:25
 */
public class RsaDemo {
    public static void main(String[] args) throws IOException {
        // openssl生成的RSA密钥默认是PEM格式。PEM长这样 ‘-----BEGIN RSA PRIVATE KEY-----’，参考resources/rsa/jwt
        // java包默认只支持DER格式
        
        //解析私钥文件
        String sshPrivateKey = getKeyByFile("rsa/jwt");
        System.out.println("sshPrivateKey: " + sshPrivateKey);
        //解析公钥文件
        String sshPublicKey = getKeyByFile("rsa/jwt.pub");
        System.out.println("sshPublicKey: " + sshPublicKey);
        

        System.out.println("方式1，根据keyPair获取公钥/私钥--------------------------------------------------");
        KeyPair keyPair = RsaKeyHelper.parseKeyPair(sshPrivateKey);
        System.out.println("private: " + keyPair.getPrivate());
        System.out.println("public: " + keyPair.getPublic());
        
        System.out.println("方式2，获取私钥-----------------------------------------------------------------");
        RSAPrivateKey rsaPrivateKey = RsaKeyHelper.loadPrivateKey(sshPrivateKey);
        System.out.println("rsaPrivateKey: " + rsaPrivateKey);
        
        System.out.println("方式3，获取公钥------------------------------------------------------------------");
        //一定要trim()，不然有换行符会导致正则表达式匹配失败
        RSAPublicKey rsaPublicKey = RsaKeyHelper.parsePublicKey(sshPublicKey.trim());
        System.out.println("rsaPublicKey: " + rsaPublicKey);

        System.out.println("方式4，参考RsaSigner-------------------------------------------------------------");
        RsaSigner rsaSigner = new RsaSigner(sshPrivateKey);
        System.out.println(rsaSigner);
    }
    
    public static String getKeyByFile(String fileName){
        try {
            try(InputStream in = ClassLoader.getSystemResourceAsStream(fileName)){
                Assert.notNull(in,fileName + " 不是有效的文件地址");
                return IOUtils.readInputStreamToString(in);
            }
        }catch (IOException e){
            throw new RuntimeException("读取密钥文件失败",e);
        }
    }

}
