package com.example.demo.settlement.application.acl;

import com.example.demo.settlement.domain.model.SettlementOrder;

import java.time.LocalDate;
import java.util.List;

public interface SettlementOrderAcl {

    List<SettlementOrder> findSettlementCandidates(LocalDate settlementDate);
}
