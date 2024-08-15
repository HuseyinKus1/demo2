package com.fmss.userservice.repository;


import com.fmss.userservice.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query(value = "SELECT * FROM refreshtokens WHERE refresh_token_status = :status and expiration_date < :now ",nativeQuery = true)
    List<RefreshToken> findAllByRefreshTokenStatusACTIVEAndExpirationDateBefore(@Param(value = "now") Instant now, @Param(value = "status") String status);

}
