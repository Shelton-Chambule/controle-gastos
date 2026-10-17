package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.exception.AccessDeniedException;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import com.chambule.controle_gastos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new AccessDeniedException("User not authenticated");
        }

        String login = extractLogin(auth);
        return userRepository.findByLogin(login).orElseThrow(() -> new ResourceNotFound(login));
    }

    private String extractLogin(Authentication auth) {
        Object principal = auth.getPrincipal();

        if (principal instanceof User user) {
            return user.getLogin();
        }

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        if (principal instanceof String login) {
            return login;
        }

        String name = auth.getName();
        if (name == null || name.isBlank() || "anonymousUser".equals(name)) {
            throw new AccessDeniedException("User not authenticated");
        }

        return name;
    }

    public boolean isAdmin(User user) {
        return user.getRole() == UserType.ADMIN;
    }

    public void assertOwnerOrAdmin(Launch launch) {
        User user = getCurrentUser();
        if (!isAdmin(user) && !launch.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Sem permissão para este lançamento");
        }
    }
}
