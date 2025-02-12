package Gestion_scolaire.configuration.SecurityConfigs;

import Gestion_scolaire.Administrators.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminInfoDetails implements UserDetails {

    private final Admin admin;


    public AdminInfoDetails(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Vérifiez que `admin` et son rôle ne sont pas nulls
        if (admin != null && admin.getIdRole() != null) {
            String roleName = admin.getIdRole().getNom();

            if (!"Admin".equalsIgnoreCase(roleName)) {
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
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getEmail();
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
        return admin.isActive();
    }


}
