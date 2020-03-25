package com.galvanize.rest.todo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.rest.todo.entities.Todo;
import com.galvanize.rest.todo.repository.TodoRepositoryInMem;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
final class TodoListControllerTest {
	@Autowired
	TodoRepositoryInMem todoRepository;

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@BeforeEach
	void beforeEach() {
		todoRepository.deleteAll();
	}

	@AfterEach
	void afterEach() {
		todoRepository.deleteAll();
	}

	@Test
	void returnEmptyTodoListByDefault() throws Exception {
		mvc.perform(get("/api/todo")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string("[]"));

	}

	@Test
	void returnOneTodoWhenOneInList() throws Exception {
		final Todo todo = new Todo("finish api");
		final List<Todo> expected = Collections.singletonList(todoRepository.save(todo));

		String json = mvc.perform(get("/api/todo")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(expected)))
				// Get response as string to parse below for ObjectMapper Example
				.andReturn().getResponse().getContentAsString();

		// Example using ObjectMapper
		List<Todo> actual = objectMapper.readValue(json, new TypeReference<List<Todo>>() {});
		assertThat(actual.size(), equalTo(1));
    	assertThat(actual, is(expected));
	}

	@Test
	void canCreateNewTodo() throws Exception {
		final String payload = "{\"text\": \"add a todo\"}";
		mvc.perform(post("/api/todo")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(payload))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.text").value("add a todo"))
				.andExpect(jsonPath("$.id").isNumber());
	}

	@Test
	void canReturnAllTodos() throws Exception {
		final Todo todoOne = new Todo("start api");
		final Todo todoTwo = new Todo("finish api");
		todoRepository.save(todoOne);
		todoRepository.save(todoTwo);
		final List<Todo> expected = todoRepository.findAll();
		String json = mvc.perform(get("/api/todo")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().json(objectMapper.writeValueAsString(expected)))
		// Get response as string to parse below for ObjectMapper Example
		.andReturn().getResponse().getContentAsString();
		List<Todo> actual = objectMapper.readValue(json, new TypeReference<List<Todo>>() {});
		assertThat(actual.size(), equalTo(2));
    	assertThat(actual, is(expected));
	}

	@Test
	void canMarkTodoAsDone() throws Exception {
		final Todo todo = new Todo("finish api");
		todoRepository.save(todo);
		final String path = todo.getId().toString();
		final String payload = "\"true\"";

		mvc.perform(get("/api/todo/" + path)
		.characterEncoding("utf-8")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.done").value(false));

		mvc.perform(put("/api/todo/done/" + path)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.characterEncoding("utf-8")
		.content(payload))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(path))
		.andExpect(jsonPath("$.text").value("finish api"))
		.andExpect(jsonPath("$.done").value(true));
	} 
	@Test
	void canChangeTodoText()  throws Exception {

		final Todo todo = new Todo("finish api");
		todoRepository.save(todo);
		final String path = todo.getId().toString();
		final String payload = "\"start api\"";

		mvc.perform(put("/api/todo/change/" + path)
		.characterEncoding("utf-8")
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(payload))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value(path))
		.andExpect(jsonPath("$.text").value("start api"));
	}
}
