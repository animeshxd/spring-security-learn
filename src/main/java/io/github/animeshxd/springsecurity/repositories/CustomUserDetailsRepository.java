package io.github.animeshxd.springsecurity.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.github.animeshxd.springsecurity.models.CustomUser;

@Repository
public interface CustomUserDetailsRepository extends CrudRepository<CustomUser, String> {
    
}
