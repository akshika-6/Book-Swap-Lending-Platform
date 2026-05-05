package com.bookswap.platform.repository;

import com.bookswap.platform.model.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
}
