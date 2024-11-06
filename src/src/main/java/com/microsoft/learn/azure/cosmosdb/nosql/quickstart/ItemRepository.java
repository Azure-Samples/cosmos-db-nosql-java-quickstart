package com.microsoft.learn.azure.cosmosdb.nosql.quickstart;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;

@Repository
public interface ItemRepository extends CosmosRepository<Item, String> {
    @Query("SELECT * FROM products p WHERE p.category = @category")
    List<Item> getItemsByCategory(@Param("category") String category);
}
