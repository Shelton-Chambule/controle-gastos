package com.chambule.controle_gastos.services;
import com.chambule.controle_gastos.entities.Launch;
import com.chambule.controle_gastos.entities.User;
import com.chambule.controle_gastos.entities.enums.UserType;
import com.chambule.controle_gastos.exception.AccessDeniedException;
import com.chambule.controle_gastos.exception.ResourceNotFound;
import com.chambule.controle_gastos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("User not authenticated");
        }
        return userRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new ResourceNotFound(auth.getName()));
    }

    public boolean isAdmin(User user){
            return  user.getRole() == UserType.ADMIN;
    }

    public void assertOwnerOrAdmin(Launch launch) {
        User user = getCurrentUser();
        if (!isAdmin(user) && !launch.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Sem permissão para este lançamento");
        }
    }

}
