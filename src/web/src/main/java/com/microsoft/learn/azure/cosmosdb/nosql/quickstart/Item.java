package com.microsoft.learn.azure.cosmosdb.nosql.quickstart;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Container(containerName = "products", autoCreateContainer = false)
public class Item {
    private String id;
    private String name;
    private Integer quantity;
    private Boolean sale;

    @PartitionKey
    private String category;

    public Item() {        
    }

    public Item(String id, String category, String name, Integer quantity, Boolean sale) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.quantity = quantity;
        this.sale = sale;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getSale() {
        return this.sale;
    }

    public void setSale(Boolean sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(this);
            return json;
        }
        catch (JsonProcessingException e) {
            return String.format("[%s]\tItem:\t%s", this.id, this.name);
        }
    }
}