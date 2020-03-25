package com.galvanize.rest.todo.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Part;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.galvanize.rest.todo.entities.Todo;
import com.galvanize.rest.todo.repository.TodoRepositoryInMem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TodoRepositoryInMem repository;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/getTodos/{range}")
    public ObjectNode getTodos(@PathVariable int range) throws JsonProcessingException {
        // Setup
        Todo todo1 = new Todo("eins");
        Todo todo2 = new Todo("nach dem");
        Todo todo3 = new Todo("Anderen");
        repository.save(todo1, todo2, todo3);

        List<Object> itemsInRange = repository.findAll().stream()
                .filter(item -> repository.findAll().indexOf(item) < range).collect(Collectors.toList());

        JsonNode childNode = objectMapper.valueToTree(itemsInRange);
        ObjectNode parentNode = objectMapper.createObjectNode();
        JsonNode anotherChildNode = objectMapper.valueToTree(repository.findAll().size());
        parentNode.putArray("entries").add(childNode);
        parentNode.set("foundObjects", anotherChildNode);
        return parentNode;
    }

}
