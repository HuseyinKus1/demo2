package com.fmss.userservice.helper;

import com.fmss.userservice.model.Packages;
import com.fmss.userservice.model.enums.PackageType;
import com.fmss.userservice.repository.PackagesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PackagesInitializer {
    private final PackagesRepository packagesRepository;

    @PostConstruct
    private void dbInitializer(){
      if(packagesRepository.findAll().isEmpty()){
        Packages package1 = Packages.builder().type(PackageType.SILVER).rightsToListing(10).validityDays(30).price(BigDecimal.valueOf(50)).build();
        Packages package2 = Packages.builder().type(PackageType.GOLD).rightsToListing(10).validityDays(45).price(BigDecimal.valueOf(70)).build();
        Packages package3 = Packages.builder().type(PackageType.PLATINUM).rightsToListing(10).validityDays(60).price(BigDecimal.valueOf(85)).build();

        packagesRepository.saveAll(List.of(package1,package2,package3));
    }}
}
