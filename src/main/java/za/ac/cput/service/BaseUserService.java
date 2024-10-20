package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.*;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public abstract class BaseUserService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    // Find a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update user password
//    public User updatePassword(Long userId, String newRawPassword) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//        String encodedPassword = passwordEncoder.encode(newRawPassword);
//        User user2 = new User.UserBuilder()
//                .copy(user)
//                .setPassword(encodedPassword)
//                .build();
//        return userRepository.save(user2);
//    }
//
//
//    // Add a role to an existing user
//    public User addRoleToUser(Long userId, String roleName) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//
//        Role role = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
//
//        Set<Role> roles = new HashSet<>(user.getRoles());
//        roles.add(role);
//        User user2 = new Student.StudentBuilder()
//                .copy(user)
//                .setRoles(roles)
//                .build();
//
//        return userRepository.save(user);
//    }

//    public User removeRoleFromUser(Long userId, String roleName) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//
//        Role role = roleRepository.findByName(roleName)
//                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
//
//        Set<Role> roles = new HashSet<>(user.getRoles());
//        if (roles.remove(role)) {
//            User user2 = new Student.StudentBuilder()
//                    .copy(user)
//                    .setRoles(roles)
//                    .build();
//        } else {
//            throw new RuntimeException("User does not have the role: " + roleName);
//        }
//
//        return userRepository.save(user);
//    }
}
