package com.example.taskthis;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterListView extends BaseAdapter {

	private static final long serialVersionUID = 1L;
	private LayoutInflater mInflater;
	private List<Task> itens;

	public AdapterListView(Context context, List<Task> tasksListView) {
		// Itens do listview.
		this.itens = tasksListView;
		// Objeto responsável por pegar o Layout do item.
		mInflater = LayoutInflater.from(context);
	}

	public void addList(List<Task> list) {
		for (Task t : list) {
			if (!itens.contains(t)) {
				itens.add(t);
			}
		}
	}

	public void removeList(List<Task> list) {
		this.itens.removeAll(list);
	}

	public int getCount() {
		return itens.size();
	}

	public Task getItem(int position) {
		return (Task) itens.get(position);
	}

	public List<Task> getItens() {
		return itens;
	}

	public void setItens(List<Task> itens) {
		this.itens = itens;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ItemSuporte itemHolder;
		// Se a view estiver nula (nunca criada), inflamos o layout nela.
		if (view == null) {
			// Infla o layout para podermos pegar as views.
			view = mInflater.inflate(R.layout.item_list, null);

			// Cria um item de suporte para não precisarmos sempre inflar as
			// mesmas informacoes.
			itemHolder = new ItemSuporte();
			itemHolder.txtTitle = ((TextView) view.findViewById(R.id.text));
			itemHolder.imgIcon = ((ImageView) view
					.findViewById(R.id.imagemview));

			// Define os itens na view.
			view.setTag(itemHolder);
		} else {
			// Se a view já existe pega os itens.
			itemHolder = (ItemSuporte) view.getTag();
		}

		// Pega os dados da lista e define os valores nos itens.
		Task item = (Task) itens.get(position);

		itemHolder.txtTitle.setText(item.getTitle());
		int iconeRid;
		if (item.getStatus().equals(Status.TODO)) {
			iconeRid = R.drawable.bola_laranja;
		} else {
			iconeRid = R.drawable.bola_cinza;
		}
		itemHolder.imgIcon.setImageResource(iconeRid);

		// Retorna a view com as informacoes.
		return view;
	}

	/**
	 * Classe de suporte para os itens do layout.
	 */
	private class ItemSuporte {

		private static final long serialVersionUID = 1L;
		ImageView imgIcon;
		TextView txtTitle;
	}
}