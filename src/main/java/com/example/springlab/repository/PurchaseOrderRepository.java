package com.example.springlab.repository;

import com.example.springlab.dto.OrderSummaryDto;
import com.example.springlab.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
 @Query("""
select o
from PurchaseOrder o
join fetch o.customer
""")
    List<PurchaseOrder> findAllWithCustomer();


    @Query("""
       select new com.example.springlab.dto.OrderSummaryDto(
           o.id, o.total, c.name
       )
       from PurchaseOrder o
       join o.customer c
       """)
    List<OrderSummaryDto> findAllSummaries();

}
