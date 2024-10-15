package com.whoimi.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricConfig;
import com.ulisesbocchio.jasyptspringboot.encryptor.SimpleAsymmetricStringEncryptor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.util.text.AES256TextEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ulisesbocchio.jasyptspringboot.util.AsymmetricCryptography.KeyFormat.PEM;


@Configuration
@EnableEncryptableProperties
public class ConfigurerJasypt {
    private static final Logger log = LoggerFactory.getLogger(ConfigurerJasypt.class);

    /**
     * @param args args
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {
        String salt = "a2lua2lt";
//        String prefix="mail@whoimi.com[";
//        String suffix="]";
//        List<String> source = Arrays.asList("whoimi", "123456");
        String username = "whoimi";
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
        try (Stream<String> lines = Files.lines(Paths.get(System.getProperty("user.home") + "/.ssh/id_rsa_pkcs8"), StandardCharsets.UTF_8);
             Stream<String> linesPem = Files.lines(Paths.get(System.getProperty("user.home") + "/.ssh/id_rsa_pkcs8.pem"), StandardCharsets.UTF_8)) {
            String privateKey = lines.collect(Collectors.joining());
            System.out.println(privateKey);
            String publicKey = linesPem.collect(Collectors.joining());
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

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        System.out.println("------------------------SimpleAsymmetricConfig ------------------------------------");


    }

}
