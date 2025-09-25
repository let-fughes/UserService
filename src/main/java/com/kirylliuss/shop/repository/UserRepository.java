package com.kirylliuss.shop.repository;

import com.kirylliuss.shop.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(List<Long> ids);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(
            "UPDATE User u SET u.name = :name, u.surname = :surname, u.birthDate = :birthDate, u.email = :email WHERE u.id = :id")
    void updateUserById(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("surname") String surname,
            @Param("birthDate") LocalDate birthDate,
            @Param("email") String email);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id IN :ids")
    List<User> findByIdInWithCards(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id = :id")
    User findByIdWithCards(@Param("id") Long id);
}
