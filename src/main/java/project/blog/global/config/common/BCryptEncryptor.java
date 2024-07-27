package project.blog.global.config.common;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptEncryptor {

    // 원본 비밀번호를  BCrypt 해시로 암호화
    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 원본 비밀번호와 해시된 비밀번호가 일치하는지 확인
    public boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

}
