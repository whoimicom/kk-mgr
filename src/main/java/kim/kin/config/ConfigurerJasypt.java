package kim.kin.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricConfig;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricStringEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.ulisesbocchio.jasyptspringboot.util.AsymmetricCryptography.KeyFormat.PEM;


/**
 * @author whoimi
 */
@Configuration
@EnableEncryptableProperties
public class ConfigurerJasypt {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String salt = "a2lua2lt";
//        String prefix="im@kin.kim[";
//        String suffix="]";
//        List<String> source = Arrays.asList("kinkim", "123456");
        String username = "kinkim";
        String password = "123456";
        System.out.println("------------------------BasicTextEncryptor ------------------------------------");
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(salt);
        String encryUsername = textEncryptor.encrypt(username);
        String encryPassword = textEncryptor.encrypt(password);
        System.out.println("BasicTextEncryptor encryUsername:" + encryUsername);
        System.out.println("BasicTextEncryptor encryPassword:" + encryPassword);
        System.out.println("BasicTextEncryptor decryptUsername:" + textEncryptor.decrypt(encryUsername));
        System.out.println("BasicTextEncryptor decryptPassword:" + textEncryptor.decrypt(encryPassword));
        System.out.println("------------------------BasicTextEncryptor ------------------------------------\n");

        System.out.println("------------------------AES256TextEncryptor ------------------------------------");
        AES256TextEncryptor aes256TextEncryptor = new AES256TextEncryptor();
        aes256TextEncryptor.setPassword(salt);
        String aes256TextEncryptorUsername = aes256TextEncryptor.encrypt(username);
        String aes256TextEncryptorPassword = aes256TextEncryptor.encrypt(password);
        System.out.println("AES256TextEncryptor aes256TextEncryptorUsername:" + aes256TextEncryptorUsername);
        System.out.println("AES256TextEncryptor encryPassword:" + aes256TextEncryptorPassword);
        System.out.println("AES256TextEncryptor decryptPassword:" + aes256TextEncryptor.decrypt(aes256TextEncryptorUsername));
        System.out.println("AES256TextEncryptor decryptPassword:" + aes256TextEncryptor.decrypt(aes256TextEncryptorPassword));
        System.out.println("------------------------AES256TextEncryptor ------------------------------------\n");
        System.out.println();

        System.out.println("------------------------SimpleAsymmetricConfig ------------------------------------");
        String privateKey = Files.lines(Paths.get(System.getProperty("user.home") + "/.ssh/id_rsa_pkcs8"), StandardCharsets.UTF_8)
                .collect(Collectors.joining());
        String publicKey = Files.lines(Paths.get(System.getProperty("user.home") + "/.ssh/id_rsa_pkcs8.pem"), StandardCharsets.UTF_8)
                .collect(Collectors.joining());
        System.out.println(privateKey);
        System.out.println(publicKey);

        SimpleAsymmetricConfig config = new SimpleAsymmetricConfig();
        config.setKeyFormat(PEM);
        config.setPrivateKey(privateKey);
        config.setPublicKey(publicKey);
        StringEncryptor stringEncryptor = new SimpleAsymmetricStringEncryptor(config);
        String encryptUsername = stringEncryptor.encrypt(username);
        String encryptPassword = stringEncryptor.encrypt(password);
        String decryptUsername = stringEncryptor.decrypt(encryptUsername);
        String decryptPassword = stringEncryptor.decrypt(encryptPassword);
        System.out.println("SimpleAsymmetricStringEncryptor entryUsername:" + encryptUsername);
        System.out.println("SimpleAsymmetricStringEncryptor entryPassword:" + encryptPassword);
        System.out.println("SimpleAsymmetricStringEncryptor decryptUsername:" + decryptUsername);
        System.out.println("SimpleAsymmetricStringEncryptor decryptPassword:" + decryptPassword);
        System.out.println("------------------------SimpleAsymmetricConfig ------------------------------------");

    }

}
