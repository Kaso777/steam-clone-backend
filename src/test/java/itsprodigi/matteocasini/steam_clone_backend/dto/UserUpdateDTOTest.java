/*


package itsprodigi.matteocasini.steam_clone_backend.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserUpdateDTOTest {

    @Test
    void testGettersAndSetters() {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUsername("user"); // Dovrebbe essere ignorato per l'utente non admin
        dto.setEmail("newemail@email.com");
        dto.setPassword("newpassword");
        dto.setRole("ROLE_USER"); // Dovrebbe essere ignorato per l'utente non admin

        // Controllo se il valore è stato assegnato correttamente
        assertEquals("newemail@email.com", dto.getEmail());
        assertEquals("newpassword", dto.getPassword());

        // Il ruolo non dovrebbe essere modificato
        assertNull(dto.getRole()); // Il ruolo non deve essere modificato per un non-admin

        // Verifica che il nome utente venga accettato, ma venga ignorato per l'utente non admin
        assertEquals("user", dto.getUsername());  // Può essere impostato, ma il valore non sarà usato se non è admin
    }
}

 */