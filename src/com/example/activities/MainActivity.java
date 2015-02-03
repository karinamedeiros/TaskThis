package com.example.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.example.taskthis.AdapterListView;
import com.example.taskthis.Pasta;
import com.example.taskthis.R;
import com.example.taskthis.Status;
import com.example.taskthis.Task;
import com.example.taskthis.TaskManager;

public class MainActivity extends Activity {
	private EditText description;
	private Button addButton;
	private ListView pastasListView;
	private ListView inboxListView;
	private String[] pastas = {"Next", "Commitment","Waiting", "Project"};
	private List<Task> tasksInbox;
	// Filtram as tarefas exibidas na tela.
	private CheckBox toDo, done;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.e("log", "Entrou em oncreate");
		setContentView(R.layout.activity_main);

		init();

		addListenerAddButton();
		addListenerListView();
		addListenerListInbox();
		addListenerEditText();

		addListenerCheckBox(toDo, Status.TODO);
		addListenerCheckBox(done, Status.DONE);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.e("log", "Entrou em onrestart");
		init();
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

	/**
	 * Adiciona o listener nos itens da lista. Quando um item eh pressionado a
	 * tela {@link TaskActivity} é criada e os dados (o item selecionado e o
	 * tasksInfo) sao enviados.
	 */
	private void addListenerListView() {
		pastasListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item =  (String) pastasListView.getAdapter().getItem(position);
				Log.e("OIOIOIOIOIOIOIOIOIOIOIO", ""+item);
				if (item.equals(Pasta.NEXT.getValor())) {
					TaskManager.getInstance().setPasta(Pasta.NEXT);
				} else if (item.equals(Pasta.COMMITMENT.getValor())) {
					TaskManager.getInstance().setPasta(Pasta.COMMITMENT);
				} else if (item.equals(Pasta.WAITING.getValor())) {
					TaskManager.getInstance().setPasta(Pasta.WAITING);
				} else {
					TaskManager.getInstance().setPasta(Pasta.PROJECT);
				}
				Intent it = new Intent(getBaseContext(), ListaTaskActivity.class);
				startActivity(it);
			}
		});
	}

	/*
	 * Inicializa as variaveis.
	 */
	private void init() {
		description = (EditText) findViewById(R.id.newTask_editText_main);
		addButton = (Button) findViewById(R.id.add_button_main);
		
		pastasListView = (ListView) findViewById(R.id.list_pastas);
		android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<String>(this.getBaseContext(),
				android.R.layout.simple_list_item_1, Arrays.asList(pastas));
		pastasListView.setAdapter(adapter);
		
		inboxListView = (ListView) findViewById(R.id.list_tarefas_novas);
		TaskManager.getInstance().setPasta(Pasta.INBOX);
		tasksInbox = TaskManager.getInstance().getTasks();
		inboxListView.setAdapter(new AdapterListView(this.getBaseContext(),
				tasksInbox));
		
		toDo = (CheckBox) findViewById(R.id.todo_checkBox_main);
		done = (CheckBox) findViewById(R.id.done_checkBox_main);
		toDo.setChecked(true);
		done.setChecked(true);
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
						TaskManager.getInstance().increaseId(), 
						Pasta.INBOX);
				description.getText().clear();
				TaskManager.getInstance().addTask(aux);
				// Limpa o campo de texto.
				description.getText().clear();
				tasksInbox.add(aux);
				inboxListView.invalidateViews();
			}
		});
	}
	
	/**
	 * Adiciona o listener nos itens da lista. Quando um item eh pressionado a
	 * tela {@link TaskActivity} é criada e os dados (o item selecionado e o
	 * tasksInfo) sao enviados.
	 */
	private void addListenerListInbox() {
		inboxListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Task item = ((AdapterListView) inboxListView.getAdapter())
						.getItem(position);
				TaskManager.getInstance().setSelectedTask(item);
				Intent it = new Intent(getBaseContext(), TaskActivity.class);
				startActivity(it);
			}
		});
	}
	
	private void addListenerEditText() {
		description.setFocusableInTouchMode(true);
		description.requestFocus();
		description.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					if (description.getText() == null
							|| description.getText().toString().replaceAll(" ", "")
							.isEmpty()) {
						return false;
					}
					Task aux = new Task(description.getText().toString(),
							TaskManager.getInstance().increaseId(),
							TaskManager.getInstance().getPasta());
					TaskManager.getInstance().addTask(aux);
					// Limpa o campo de texto.
					description.getText().clear();
					tasksInbox.add(aux);
					inboxListView.invalidateViews();
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
				inboxListView.invalidateViews();
			}
		});
	}

	private void removeTasks(Status status) {
		List<Task> auxList = new ArrayList<Task>(tasksInbox);
		for (Task t : auxList) {
			if (t.getStatus().equals(status)) {
				tasksInbox.remove(t);
			}
		}
	}

	private void addTasks(Status status) {
		for (Task t : TaskManager.getInstance().getTasks()) {
			if (t.getStatus().equals(status) && !tasksInbox.contains(t)) {
				tasksInbox.add(t);
			}
		}
	}
}