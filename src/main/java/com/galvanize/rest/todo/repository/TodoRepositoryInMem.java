package com.galvanize.rest.todo.repository;

import com.galvanize.rest.todo.entities.Todo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TodoRepositoryInMem {
	private List<Todo> todos = new ArrayList<>();
	private Long nextId = 0L;

	public List<Todo> findAll() {
		return todos;
	}

	public Todo save(Todo item) {
		if (item.getId() == null) {
			item.setId(nextId++);
		}
		this.todos.add(item);
		return item;

	}

	public List<Todo> save(Todo... todo) {
		for (Todo item : todo) {
			if (item.getId() == null) {
				item.setId(nextId++);
			}
			this.todos.add(item);
		}
		return this.todos;
	}

	public void deleteAll() {
		todos.clear();
	}

	public Todo findObjectById(long id) {
		return this.todos.stream().filter(td -> td.getId() == id).findFirst().orElse(null);
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
