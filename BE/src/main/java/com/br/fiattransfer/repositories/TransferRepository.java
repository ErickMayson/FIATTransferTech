package com.br.fiattransfer.repositories;

import com.br.fiattransfer.models.scheduler.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
