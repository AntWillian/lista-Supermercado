package br.com.listamercado;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText txtproduto;

    ProdutoAdapter adapter;

    View.OnClickListener clic_ok = new View.OnClickListener(){
        @Override
        public void onClick (View view){



            CheckBox ck = (CheckBox) view;

            int posicao = (int) ck.getTag();

            Produto produtoselecionada = adapter.getItem(posicao);

            Produto produtoDB = Produto.findById(Produto.class,produtoselecionada.getId());



            if (ck.isChecked()){

                produtoDB.setAtivo(true);
                produtoselecionada.setAtivo(true);
                produtoDB.save();

            }else{
                produtoDB.setAtivo(false);
                produtoselecionada.setAtivo(false);
                produtoDB.save();
            }

            adapter.notifyDataSetChanged();

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        txtproduto = (EditText) findViewById(R.id.txtProduto);

       List<Produto> lstProdutos = Produto.listAll(Produto.class);

//        lstProdutos.add( new Produto("Feijao",false));
//        lstProdutos.add( new Produto("Arroz",false));
//        lstProdutos.add( new Produto("Macarrao",true));

        adapter = new ProdutoAdapter(this , lstProdutos);

        listView.setAdapter(adapter);

    }

    public void inserirItem(View view) {


        String produto =txtproduto.getText().toString();

        if (produto.isEmpty()){
            Toast.makeText(this,"prencha o campo",Toast.LENGTH_LONG).show();
            return;}

        Produto p = new Produto(produto , false);

        adapter.add(p);

        p.save();

        txtproduto.setText(null);

    }

    // classe do adapter

    private class ProdutoAdapter extends ArrayAdapter<Produto>{

        public  ProdutoAdapter (Context ctx, List<Produto> items){
            super(ctx,0,items);



        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View v = convertView;

            if (v == null){
                 v= LayoutInflater.from(getContext()).inflate(R.layout.itemlista,null);
            }

                Produto item = getItem(position);

            TextView txt_item_produto= v.findViewById(R.id.txt_item_produto);
            CheckBox ck_item_produto = v.findViewById(R.id.ck_item_produto);

            txt_item_produto.setText(item.getNome());
            ck_item_produto.setChecked(item.isAtivo());

            ck_item_produto.setTag(position);

            ck_item_produto.setOnClickListener(clic_ok);

            return v;
        }
    }
}
