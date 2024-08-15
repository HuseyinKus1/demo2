package com.fmss.userservice.repository;

import com.fmss.userservice.model.UserPackages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserPackagesRepository extends JpaRepository<UserPackages,Long> {

    @Query(value = "SELECT up.* FROM \"users-packages\" up " +
            "JOIN \"packages\" p ON p.id = up.packages_id " +
            "WHERE up.user_id = :userId " +
            "AND p.type = :packageType",
            nativeQuery = true)
    UserPackages findByUserIdAndPackageType(@Param("userId") Long userId,
                                                              @Param("packageType") String packageType);
    @Query(value = "SELECT * FROM \"public\".\"users-packages\" WHERE user_id = :id", nativeQuery = true)
    Page<UserPackages> findByUserIdAndPageable(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT up.* FROM \"users-packages\" up " +
            "JOIN \"packages\" p ON p.id = up.packages_id " +
            "WHERE up.user_id = :userId " +
            "AND p.type = :packageType",
            nativeQuery = true)
    Optional<UserPackages> findByUserIdAndPackageTypeOptional(@Param("userId") Long userId,
                                                              @Param("packageType") String packageType);

}
