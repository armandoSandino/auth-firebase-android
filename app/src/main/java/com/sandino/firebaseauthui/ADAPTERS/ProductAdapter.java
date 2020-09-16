package com.sandino.firebaseauthui.ADAPTERS;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.sandino.firebaseauthui.MODELS.Product;
import com.sandino.firebaseauthui.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductAdapterHolder>{
    List<Product> lista;
    int layout;
    Context context;
    ProductAdapter.MiClickItemListener listener;
    public ProductAdapter(List<Product> lista , int layout, Context context , ProductAdapter.MiClickItemListener  lis ){
        this.lista = lista;
        this.layout = layout;
        this.context = context;
        this.listener = lis;
    }
    @NonNull
    @Override
    public ProductAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( layout , parent , false);
        //RecyAdapCasasHolder holder = new RecyAdapCasas.RecyAdapCasasHolder(view);
        return new ProductAdapter.ProductAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapterHolder holder, int position) {
        holder.bind( lista.get(position) , listener );
    }

    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    public class ProductAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Context contextH;
        ImageView imvProductImage;
        TextView lbProductName ,lbProductLink;

        public ProductAdapterHolder(View itemView) {
            super(itemView);
            contextH = itemView.getContext();

            lbProductLink = itemView.findViewById(R.id.lbProductLink);
            lbProductName =  itemView.findViewById(R.id.lbProductName);
            imvProductImage = itemView.findViewById(R.id.imvProductImage);
        }

        //metodo que se encarga de quemar la informacion en cada elemento de la lista y establecer sus eventos click
        void bind( final Product dato ,final ProductAdapter.MiClickItemListener escuchador ){

            Picasso.get()
                    //.load(android.R.drawable.alert_dark_frame )
                    .load( dato.getImage() )
                    .placeholder(android.R.drawable.arrow_down_float)
                    .error(android.R.drawable.stat_notify_error)
                    .into(this.imvProductImage);
            this.lbProductName.setText( dato.getName() );
            this.lbProductLink.setText( dato.getLink() );

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    escuchador.MiClickItem( dato , getAdapterPosition() );
                }
            });
        }
        @Override
        public void onClick(View v) {
            /*switch (v.getId() ){
                case R.id.bt_item_llamar:{
                    if ( lista.get( getAdapterPosition() ).getTelefono().length()>=8){
                        Intent llamar = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +lista.get(getAdapterPosition()).getTelefono() ) );
                        if (ActivityCompat.checkSelfPermission( contextH , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        contextH.startActivity(llamar);
                    }else {
                        Toast.makeText(contextH, "Nùmero Indefinido", Toast.LENGTH_SHORT).show();
                    }
                }break;
                case R.id.bt_item_contactar:{
                    Intent intent = new Intent(contextH , Contactar.class);
                    intent.putExtra("id_casa",String.valueOf( lista.get(getAdapterPosition()).getId()) );
                    intent.putExtra("id_user",String.valueOf( 420 ) );
                    contextH.startActivity( intent );
                }break;
                default:break;
            }*/
        }
    }
    //definimos nuestra propia interfaz,para manejar el evento click
    public interface MiClickItemListener{
        void MiClickItem(Product product, int position );
    }
}