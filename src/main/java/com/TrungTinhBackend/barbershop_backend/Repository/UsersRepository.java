package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long>, JpaSpecificationExecutor<Users> {
    Users findByUsernameAndIsDeleted(String username, boolean isDeleted);
    Users findByEmail(String email);
    Optional<Users> findByPhoneNumber(String phoneNumber);
    Users findByUsername(String username);

    Users findByPhoneNumberAndIsDeleted(String phoneNumber, boolean b);
}
