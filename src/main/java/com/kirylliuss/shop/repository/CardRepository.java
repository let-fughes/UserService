package com.kirylliuss.shop.repository;

import com.kirylliuss.shop.model.Card;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c WHERE c.userId = :userId")
    List<Card> findByUserId(@Param("userId") Long userId);

    List<Card> findByIdIn(List<Long> ids);

    @Modifying
    @Transactional
    @Query(
            "UPDATE Card c SET c.number = :number, c.holder = :holder, c.expirationDate = :expirationDate WHERE c.id = :id")
    void updateCardById(
            @Param("id") Long id,
            @Param("number") String number,
            @Param("expirationDate") LocalDate expirationDate,
            @Param("holder") String holder);
}
