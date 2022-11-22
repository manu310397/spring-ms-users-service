package com.manoj.users.repo;

import com.manoj.users.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
