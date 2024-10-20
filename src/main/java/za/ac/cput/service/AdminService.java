package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.*;
import za.ac.cput.repository.AdminRepository;
import za.ac.cput.repository.RoleRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class AdminService implements IAdminService{
    @Autowired
    private  AdminRepository adminRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin save(Admin admin) {
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_ADMIN"));

        Admin admin2 = new Admin.AdminBuilder()
                .copy(admin)
                .setPassword(encodedPassword)
                .setRoles(Collections.singleton(adminRole))
                .build();

        return adminRepository.save(admin2);
    }

    @Override
    public Admin read(Long adminId) {
        return adminRepository.findById(adminId).orElse(null);
    }

    @Override
    public Admin update(Admin admin) {
        return adminRepository.save(admin);
    }


    @Override
    public boolean deleteById(Long adminId) {
        adminRepository.deleteById(adminId);
     return !adminRepository.existsById(adminId);
    }
    @Override
    public List<Admin> getAll() {
        return adminRepository.findAll();
    }
}
