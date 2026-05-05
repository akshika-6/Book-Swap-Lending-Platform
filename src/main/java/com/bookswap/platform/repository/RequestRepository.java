package com.bookswap.platform.repository;

import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Request;
import com.bookswap.platform.model.RequestStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequester(AppUser requester);

    List<Request> findByOwner(AppUser owner);

    List<Request> findByStatus(RequestStatus status);
}