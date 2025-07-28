package itsprodigi.matteocasini.steam_clone_backend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPasswordAdmin = encoder.encode("adminpass");
        String hashedPasswordUser = encoder.encode("userpass");

        System.out.println("Hashed adminpass: " + hashedPasswordAdmin);
        System.out.println("Hashed userpass: " + hashedPasswordUser);
    }
}