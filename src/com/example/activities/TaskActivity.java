package com.example.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.taskthis.Pasta;
import com.example.taskthis.R;
import com.example.taskthis.Status;
import com.example.taskthis.TaskManager;

/**
 * Tela de detalhes/edicao de tarefas.
 * 
 */
public class TaskActivity extends Activity {

	// ==== Componentes da IU ==== //
	private EditText description;
	private RadioGroup radios;
	private RadioButton inbox;
	private RadioButton next;
	private RadioButton waiting;
	private RadioButton project;
	private Button saveButton;
	private Button deleteButton;
	private Button doneButton;
	private CheckBox todo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);

		init();
		// Altera o texto do editor de campo para a descricao da tarefa
		// recebida.
		description.setText(TaskManager.getInstance().getSelectedTask()
				.getDescription());
		// Seleciona o radio corresnpondente a pasta da tarefa recebida.
		Pasta pasta = TaskManager.getInstance().getSelectedTask().getPasta();
		if (pasta.equals(Pasta.INBOX)) {
			radios.check(R.id.radio_inbox);
		} else if (pasta.equals(Pasta.WAITING)) {
			radios.check(R.id.radio_waiting);
		} else if (pasta.equals(Pasta.PROJECT)) {
			radios.check(R.id.radio_project);
		} else if (pasta.equals(Pasta.NEXT)) {
			radios.check(R.id.radio_next);
		} else {
			radios.check(R.id.radio_commitment);
		}
		
		if (TaskManager.getInstance().getSelectedTask().getStatus().equals(Status.TODO)) {
			todo.setVisibility(View.INVISIBLE);
		} else {
			doneButton.setVisibility(View.INVISIBLE);
		}
		addListenerSaveButton();
		addListenerDeleteButton();
		addListenerDoneButton();
	}

	/**
	 * Inicializa as variaveis.
	 */
	private void init() {
		description = (EditText) findViewById(R.id.description_edittext);
		radios = (RadioGroup) findViewById(R.id.radio_pastas);
		inbox = (RadioButton) findViewById(R.id.radio_inbox);
		next = (RadioButton) findViewById(R.id.radio_next);
		waiting = (RadioButton) findViewById(R.id.radio_waiting);
		project = (RadioButton) findViewById(R.id.radio_project);
		saveButton = (Button) findViewById(R.id.save_button);
		deleteButton = (Button) findViewById(R.id.delete_button);
		doneButton = (Button) findViewById(R.id.done_button);
		todo = (CheckBox) findViewById(R.id.check_mudar_para_todo);
	}

	/**
	 * Adiciona o listener do botao salvar.
	 */
	private void addListenerSaveButton() {
		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (description.getText() == null
						|| description.getText().toString().replaceAll(" ", "")
								.isEmpty()) {
					return;
				}
				TaskManager.getInstance().getSelectedTask()
						.setDescription(description.getText().toString());

				if (inbox.isChecked()) {
					TaskManager.getInstance().getSelectedTask()
							.setPasta(Pasta.INBOX);
				} else if (waiting.isChecked()) {
					TaskManager.getInstance().getSelectedTask()
							.setPasta(Pasta.WAITING);
				} else if (project.isChecked()) {
					TaskManager.getInstance().getSelectedTask()
							.setPasta(Pasta.PROJECT);
				} else if (next.isChecked()) {
					TaskManager.getInstance().getSelectedTask()
							.setPasta(Pasta.NEXT);
				} else {
					TaskManager.getInstance().getSelectedTask()
							.setPasta(Pasta.COMMITMENT);
				}
				
				if (todo.isChecked()) {
					TaskManager.getInstance().getSelectedTask().setStatus(Status.TODO);
				}
				
				TaskManager.getInstance().refreshSelectedTask();
				finish();
			}
		});
	}

	private void addListenerDeleteButton() {
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskManager.getInstance().removeSelectedTask();
				finish();
			}
		});
	}
	
	private void addListenerDoneButton() {
		doneButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TaskManager.getInstance().getSelectedTask()
						.setStatus(Status.DONE);
				TaskManager.getInstance().refreshSelectedTask();
				finish();
			}
		});
	}
}