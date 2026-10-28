package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.exception.AccessDeniedException;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import com.chambule.controle_gastos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        log.debug("Resolving current authenticated user");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            log.warn("Attempt to resolve current user without a valid authentication");
            throw new AccessDeniedException("User not authenticated");
        }

        String login = extractLogin(auth);
        return userRepository.findByLogin(login).orElseThrow(() -> {
            log.warn("Authenticated login='{}' not found in database", login);
            return new ResourceNotFound(login);
        });
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
            log.warn("Unable to extract a valid login from authentication principal");
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
            log.warn("User id={} denied access to launch id={} owned by user id={}",
                    user.getId(), launch.getId(), launch.getUser().getId());
            throw new AccessDeniedException("Sem permissão para este lançamento");
        }
    }
}