package com.fmss.userservice.repository;

import com.fmss.userservice.model.Packages;
import com.fmss.userservice.model.enums.PackageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackagesRepository extends JpaRepository<Packages, Long> {
    Optional<Packages> findByType(PackageType type);
}

