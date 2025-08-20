package com.example.hotelbookingsystem.repository;

import com.example.hotelbookingsystem.Models.ActionType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionTypeRepository extends JpaRepository<ActionType,Long> {


    @Query(
            " select a " +
            " from ActionType a" +
            " where a.actionName = :actionName"

    )
    ActionType findByName(@Param("actionName")String actionName);
}
