package za.ac.cput.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Document;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserPrincipal;
import za.ac.cput.repository.ContactRepository;
import za.ac.cput.repository.UserRepository;
import za.ac.cput.service.JWTService;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private JWTService jwtService;


    @Override
    public User save(User user) {
        return repo.save(user);
    }

    @Override
    public User read(Long aLong) {
        return repo.findById(aLong).orElse(null);
    }

    @Override
    public User update(User user) {
        return repo.save(user);
    }

    @Override
    public boolean deleteById(Long aLong) {
        repo.deleteById(aLong);
        return !repo.existsById(aLong);
    }

    @Override
    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repo.findByEmail(username);
        return user.map(u -> new UserPrincipal.Builder().setUser(u).build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
    public String verify(User user, AuthenticationManager authManager) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
        );
        if (authentication.isAuthenticated()) {
            String role = user.getRoles().iterator().next().getName(); // Assumes there's at least one role
            return jwtService.generateToken(user.getEmail(), role);
        } else {
            return "fail";
        }
    }


}
