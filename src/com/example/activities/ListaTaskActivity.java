package com.example.activities;

import java.util.ArrayList;
import java.util.List;

import com.example.taskthis.AdapterListView;
import com.example.taskthis.R;
import com.example.taskthis.Status;
import com.example.taskthis.Task;
import com.example.taskthis.TaskManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListaTaskActivity extends Activity {
	private List<Task> tasksListView;

	// ==== Componentes da IU ==== //
	// Filtram as tarefas exibidas na tela.
	private CheckBox toDo, done;
	// Lista de tarefas que sao exibidas na tela.
	private ListView listView;
	// Campo de texto para criacao de novas tarefas.
	private EditText description;
	private Button addButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e("log", "Entrou em oncreate");
		setContentView(R.layout.activity_lista);

		init();

		addListenerEditText();
		addListenerAddButton();
		addListenerListView();

		addListenerCheckBox(toDo, Status.TODO);
		addListenerCheckBox(done, Status.DONE);

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.e("log", "Entrou em onrestart");

		tasksListView = new ArrayList<Task>(TaskManager.getInstance()
				.getTasks());

		listView.setAdapter(new AdapterListView(this.getBaseContext(),
				tasksListView));
		listView.invalidateViews();

		toDo.setChecked(true);
		done.setChecked(true);
	}

	@Override
	public void onBackPressed() {
		TaskManager.getInstance().getTasks().clear();
		finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		newConfig.orientation = Configuration.ORIENTATION_PORTRAIT;
		super.onConfigurationChanged(newConfig);

	}

	private void addListenerAddButton() {
		addButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (description.getText() == null
						|| description.getText().toString().replaceAll(" ", "")
								.isEmpty()) {
					return;
				}
				Task aux = new Task(description.getText().toString(),
						TaskManager.getInstance().increaseId(), TaskManager
								.getInstance().getPasta());
				description.getText().clear();
				TaskManager.getInstance().addTask(aux);
				// Limpa o campo de texto.
				description.getText().clear();
				if (toDo.isChecked()) {
					tasksListView.add(aux);
				}
				listView.invalidateViews();
			}
		});
	}

	/**
	 * Adiciona o listener no campo de texto. Toda vez que a tecla ENTER é
	 * pressionada o método onkey é chamado.
	 */
	private void addListenerEditText() {
		description.setFocusableInTouchMode(true);
		description.requestFocus();
		description.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					if (description.getText() == null
							|| description.getText().toString().equals("")) {
						return false;
					}
					Task aux = new Task(description.getText().toString(),
							TaskManager.getInstance().increaseId(), TaskManager
									.getInstance().getPasta());
					TaskManager.getInstance().addTask(aux);
					// Limpa o campo de texto.
					description.getText().clear();
					if (toDo.isChecked()) {
						tasksListView.add(aux);
					}
					listView.invalidateViews();
				}
				return false;
			}
		});
	}

	/**
	 * Adiciona o listener no checkbox passado como parametro. Quando o checkbox
	 * eh clicado o metodo onClick eh chamado e as lista de exibicao das tarefas
	 * eh atualizado.
	 * 
	 * @param checkBox
	 * @param list
	 *            Lista de tarefas com status correspondentes ao checkbox(to do,
	 *            doing ou done).
	 */
	private void addListenerCheckBox(final CheckBox checkBox,
			final Status status) {
		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (checkBox.isChecked()) {
					addTasks(status);
				} else {
					removeTasks(status);
				}
				listView.invalidateViews();
			}
		});
	}

	private void removeTasks(Status status) {
		List<Task> auxList = new ArrayList<Task>(tasksListView);
		for (Task t : auxList) {
			if (t.getStatus().equals(status)) {
				tasksListView.remove(t);
			}
		}
	}

	private void addTasks(Status status) {
		for (Task t : TaskManager.getInstance().getTasks()) {
			if (t.getStatus().equals(status) && !tasksListView.contains(t)) {
				tasksListView.add(t);
			}
		}
	}

	/**
	 * Adiciona o listener nos itens da lista. Quando um item eh pressionado a
	 * tela {@link TaskActivity} é criada e os dados (o item selecionado e o
	 * tasksInfo) sao enviados.
	 */
	private void addListenerListView() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Task item = ((AdapterListView) listView.getAdapter())
						.getItem(position);
				TaskManager.getInstance().setSelectedTask(item);
				Intent it = new Intent(getBaseContext(), TaskActivity.class);
				startActivity(it);
			}
		});
	}

	/*
	 * Inicializa as variaveis.
	 */
	private void init() {
		listView = (ListView) findViewById(R.id.list);

		tasksListView = TaskManager.getInstance().getTasks();

		listView.setAdapter(new AdapterListView(this.getBaseContext(),
				tasksListView));

		description = (EditText) findViewById(R.id.newTask_editText);
		addButton = (Button) findViewById(R.id.add_button);

		toDo = (CheckBox) findViewById(R.id.todo_checkBox);
		done = (CheckBox) findViewById(R.id.done_checkBox);

		toDo.setChecked(true);
		done.setChecked(true);
	}
}
