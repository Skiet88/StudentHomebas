package za.ac.cput.service.landlordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.*;
import za.ac.cput.repository.ContactRepository;
import za.ac.cput.repository.DocumentRepository;
import za.ac.cput.repository.LandlordRepository;
import za.ac.cput.repository.RoleRepository;
import za.ac.cput.service.ContactService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LandlordService implements ILandlordService{
    @Autowired
    private LandlordRepository landlordRepository;
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Landlord save(Landlord landlord) {

        documentRepository.saveAll(landlord.getDocuments());
        contactService.save(landlord.getContact());
        String encodedPassword = passwordEncoder.encode(landlord.getPassword());
        Role studentRole = roleRepository.findByName("ROLE_LANDLORD")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_STUDENT"));

        Landlord l2 = new Landlord.LandlordBuilder()
                .copy(landlord)
                .setPassword(encodedPassword)
                .setRoles(Collections.singleton(studentRole))
                .build();

        return landlordRepository.save(l2);
    }

    @Override
    public Landlord read(Long aLong) {
        return landlordRepository.findById(aLong).orElse(null);
    }

    @Override
    public Landlord update(Landlord landlord) {
        return landlordRepository.save(landlord);
    }

    @Override
    public boolean deleteById(Long aLong) {
        landlordRepository.deleteById(aLong);
        return !landlordRepository.existsById(aLong);

    }

    @Override
    public List<Landlord> getall() {
        return landlordRepository.findAll();
    }
}
