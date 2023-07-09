package com.vcriate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vcriate.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByUsernameAndPassword(String username, String password);

	@Query("SELECT u FROM User u LEFT JOIN FETCH u.history h WHERE u.id = :userId ORDER BY h.timestamp DESC")
    public User findByIdWithSortedHistory(@Param("userId") int userId);
}
