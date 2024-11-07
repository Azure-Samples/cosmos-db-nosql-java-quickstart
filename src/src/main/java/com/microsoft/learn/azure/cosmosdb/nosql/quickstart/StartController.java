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
            Item item = new Item(
                "aaaaaaaa-0000-1111-2222-bbbbbbbbbbbb",
                "gear-surf-surfboards",
                "Yamba Surfboard",
                12,
                false
            );
            Item created_item = repository.save(item);

            this.sendMessage(String.format("Upserted item:\t%s", created_item));
        }

        {
            Item item = new Item(
                "bbbbbbbb-1111-2222-3333-cccccccccccc",
                "gear-surf-surfboards",
                "Kiama Classic Surfboard",
                4,
                true
            );
            Item created_item = repository.save(item);
            this.sendMessage(String.format("Upserted item:\t%s", created_item));
        }

        PartitionKey partitionKey = new PartitionKey("gear-surf-surfboards");
        Optional<Item> existing_item = repository.findById("aaaaaaaa-0000-1111-2222-bbbbbbbbbbbb", partitionKey);
        if (existing_item.isPresent()) {
            // Do something  
        }

        existing_item.ifPresent(i -> {
            this.sendMessage(String.format("Read item id:\t%s", i.getId()));
            this.sendMessage(String.format("Read item:\t%s", i));
        });

        List<Item> items = repository.getItemsByCategory("gear-surf-surfboards");
        for (Item item : items) {
            // Do something
        }

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
