package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long>, JpaSpecificationExecutor<Users> {
    Users findByUsernameAndIsDeleted(String username, boolean isDeleted);
    Users findByEmail(String email);
    Users findByUsername(String username);
}
