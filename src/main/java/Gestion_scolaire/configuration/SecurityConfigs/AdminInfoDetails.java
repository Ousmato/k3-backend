package Gestion_scolaire.configuration.SecurityConfigs;

import Gestion_scolaire.Administrators.entity.AdministrationUsers;
import Gestion_scolaire.Administrators.entity.Postes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class AdminInfoDetails implements UserDetails {

    private final AdministrationUsers administrationUsers;



    public AdminInfoDetails(AdministrationUsers admin) {
        this.administrationUsers = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Vérifiez que `admin` et son rôle ne sont pas nulls
        if (administrationUsers != null && administrationUsers.getIdPoste() != null) {
            String roleName = administrationUsers.getIdPoste().getNom();

            if (!"SUPER_ADMIN".equalsIgnoreCase(roleName)) {
                // Créer une abréviation basée sur les mots du nom du rôle
                String[] words = roleName.split(" ");
                StringBuilder abbreviation = new StringBuilder();

                for (String word : words) {
                    if (word.length() > 3) {
                        abbreviation.append(word.charAt(0)); // Ajouter la première lettre
                    }
                }

                // Construire le nom du rôle abrégé
                String abbreviatedRoleName = abbreviation.toString().toUpperCase();

                // Ajouter le rôle avec le préfixe "ROLE_"
                authorities.add(new SimpleGrantedAuthority("ROLE_" + abbreviatedRoleName));
            }

            // Ajouter un rôle "ROLE_Admin" par défaut
            authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
        }

        return authorities;
    }


    @Override
    public String getPassword() {
        return administrationUsers.getPassword();
    }

    @Override
    public String getUsername() {
        return administrationUsers.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return administrationUsers.isActive();
    }


}
