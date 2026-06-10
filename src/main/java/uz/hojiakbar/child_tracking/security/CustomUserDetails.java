package uz.hojiakbar.child_tracking.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * CustomUserDetails — Spring Security uchun universal wrapper.
 *
 * Ikki turdagi foydalanuvchini qo'llab-quvvatlaydi:
 *   - Users  (ota-ona, PARENT / ADMIN)
 *   - Child  (bola, CHILD)
 *
 * Controller da ishlatish:
 *
 *   @AuthenticationPrincipal CustomUserDetails userDetails
 *
 *   userDetails.getUsers()   → Users  obyektini olish (null bo'lishi mumkin)
 *   userDetails.getChild()   → Child  obyektini olish (null bo'lishi mumkin)
 *   userDetails.isParent()   → ota-ona ekanligini tekshirish
 *   userDetails.isChild()    → bola ekanligini tekshirish
 *   userDetails.getId()      → UUID (kim bo'lishidan qat'i nazar)
 */
public class CustomUserDetails implements UserDetails {

    @Getter
    private final Users users;

    @Getter
    private final Child child;


    public CustomUserDetails(Users users) {
        this.users = users;
        this.child = null;
    }

    public CustomUserDetails(Child child) {
        this.child = child;
        this.users = null;
    }


    /** Kim ekanligini bilmasdan UUID olish */
    public UUID getId() {
        return isParent() ? users.getId() : child.getId();
    }

    /** Kim ekanligini bilmasdan to'liq ismni olish */
    public String getFullName() {
        return isParent() ? users.getFull_name() : child.getFull_name();
    }

    public boolean isParent() {
        return users != null;
    }

    public boolean isChild() {
        return child != null;
    }

    public UserRole getRole() {
        if (isParent()) return users.getRole();
        return UserRole.CHILD;
    }


    @Override
    public String getUsername() {
        if (isParent()) return users.getEmail();
         return (child.getPhone() != null) ? child.getPhone() : child.getEmail();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = isParent() ? users.getRole().name() : UserRole.CHILD.name();
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return "";
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
        return isParent()
                ? Boolean.TRUE.equals(users.getIsActive())
                : Boolean.TRUE.equals(child.getIsActive());
    }
}