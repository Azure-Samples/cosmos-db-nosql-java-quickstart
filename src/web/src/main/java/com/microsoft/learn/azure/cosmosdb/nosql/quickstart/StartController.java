package com.microsoft.learn.azure.cosmosdb.nosql.quickstart;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Controller
public class StartController {
    
    private static SimpMessageSendingOperations messageSendingOperations;

    private ItemRepository repository;

    @Autowired
    public StartController(SimpMessageSendingOperations messageSendingOperations, ItemRepository repository) {
        StartController.messageSendingOperations = messageSendingOperations;
        this.repository = repository;
    }

    @MessageMapping("/start")
    public void start() throws Exception {
        this.sendMessage("Current Status:\tStarting...");

        repository.deleteAll();

        this.sendMessage(String.format("Get database:\t%s", "cosmicworks"));

        this.sendMessage(String.format("Get container:\t%s", "products"));

        {
            // <create_item>
            Item item = new Item(
                "70b63682-b93a-4c77-aad2-65501347265f",
                "gear-surf-surfboards",
                "Yamba Surfboard",
                12,
                false
            );
            Item created_item = repository.save(item);
            // </create_item>
            this.sendMessage(String.format("Upserted item:\t%s", created_item));
        }

        {
            Item item = new Item(
                "25a68543-b90c-439d-8332-7ef41e06a0e0",
                "gear-surf-surfboards",
                "Kiama Classic Surfboard",
                4,
                true
            );
            Item created_item = repository.save(item);
            this.sendMessage(String.format("Upserted item:\t%s", created_item));
        }

        // <read_item>
        PartitionKey partitionKey = new PartitionKey("gear-surf-surfboards");
        Optional<Item> existing_item = repository.findById("70b63682-b93a-4c77-aad2-65501347265f", partitionKey);
        if (existing_item.isPresent()) {
            // Do something  
        }
        // </read_item>
        existing_item.ifPresent(i -> {
            this.sendMessage(String.format("Read item id:\t%s", i.getId()));
            this.sendMessage(String.format("Read item:\t%s", i));
        });

        // <query_items>
        List<Item> items = repository.getItemsByCategory("gear-surf-surfboards");
        for (Item item : items) {
            // Do something
        }
        // </query_items>
        this.sendMessage("Found items:");
        try {
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = writer.writeValueAsString(items);
            this.sendMessage(json);
        }
        catch (JsonProcessingException e) {
            this.sendMessage(String.format("Number of items:\t%s", items.size()));
        }
    }

    private void sendMessage(String message)
    {
        StartController.messageSendingOperations.convertAndSend("/topic/output", new Payload(message));
    }
}
