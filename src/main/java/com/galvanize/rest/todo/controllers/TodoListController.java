package com.galvanize.rest.todo.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.galvanize.rest.todo.entities.Todo;
import com.galvanize.rest.todo.repository.TodoRepositoryInMem;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/todo")
public final class TodoListController {

	@Autowired
	TodoRepositoryInMem todoRepository;

	@GetMapping("/{id}")
	public Todo getTodoById(@PathVariable long id, HttpServletResponse response) throws IOException {
		Todo result = todoRepository.findObjectById(id);
		if (result == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todo nicht gefunden... du Noob");
			return null;
		} else {
			return result;
		}
	}

	@GetMapping
	List<Todo> getAllTodos() {
		return todoRepository.findAll();
	}

	@PostMapping()
	public Todo saveNewTodo(@RequestBody JsonNode todoItem, HttpServletResponse response) throws IOException {

		String body = todoItem.get("text").textValue();
		if (body == null || body.length() == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Jason sollte nicht leer sein... du Noob");
			return null;
		}
		Todo newTodo = new Todo(todoItem.get("text").textValue());
		this.todoRepository.save(newTodo);
		response.setStatus(HttpServletResponse.SC_CREATED);
		return newTodo;
	}

	@PutMapping("/done/{id}")
	public Todo markTodo(@RequestBody JsonNode bool, @PathVariable long id, HttpServletResponse response)
			throws IOException {
		System.out.println(">>>>>>>>>>>>>>>>>>>>> " + bool.size());
		if (bool.asText().length() <= 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Jason sollte nicht leer sein... du Noob");
			return null;
		}
		Todo changedTodo = this.todoRepository.setItemToIsDone(id, bool.asBoolean());
		if (changedTodo == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todo nicht gefunden...");
			return null;
		}

		return changedTodo;
	}

	@PutMapping("/change/{id}")
	public Todo changeTodoText(@RequestBody JsonNode text, @PathVariable long id, HttpServletResponse response)
			throws IOException {

		String body = text.asText();
		if (body == null || body.length() == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Jason sollte nicht leer sein... du Noob");
			return null;
		}
		Todo changedTodo = this.todoRepository.setItemText(id, body);
		if (changedTodo == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todo nicht gefunden...");
			return null;
		}
		return changedTodo;
	}
}
