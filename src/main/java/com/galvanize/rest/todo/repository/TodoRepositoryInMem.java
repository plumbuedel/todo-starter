package com.galvanize.rest.todo.repository;

import com.galvanize.rest.todo.entities.Todo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TodoRepositoryInMem  {
	private List<Todo> todos = new ArrayList<>();
	private Long nextId = 0L;

	public List<Todo> findAll() {
		return todos;
	}

	public Todo save(Todo todo) {
		if (todo.getId() == null) {
			todo.setId(nextId++);
			todos.add(todo);
		}
		return todo;
	}

	public void deleteAll() {
		todos.clear();
	}

	public Todo findObjectById(long id) {
		return this.todos.stream()
		.filter(td -> td.getId() == id)
		.findFirst().orElse(null);
	}

	public Todo setItemToIsDone(long id, boolean bool) {
		Todo result = this.findObjectById(id);
		if (result != null) {
			result.setDone(bool);
			return result;
		}
		return null;
}

	public Todo setItemText(long id, String text) {
		Todo result = this.findObjectById(id);
		if (result != null) {
			result.setText(text);
			return result;
		}
		return null;
	}
}
