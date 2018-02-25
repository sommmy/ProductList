package ng.com.eliconcepts.productlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Somee on 17/12/2017.
 */
//this guys kinda like arranges everything, just like the guy that sorts a bunch of forms filled by clients

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<ListUnit> listUnitList;
    private Context context;

    //create a class accessible from othe activities
    public ListAdapter(Context context, List<ListUnit> listUnitList){
        this.context = context;
        this.listUnitList = listUnitList;

    }

    @Override
    public ListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //this method specifies the layout resource to be used, which is list_unit.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_unit,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter.MyViewHolder holder, int position) {
        //this method helps to get the position, and set parameters, more like feeling a spreadsheet of different columns and rows from a form filled by a client
        ListUnit listUnit = listUnitList.get(position);
//fill up contents here
        holder.productName.setText(listUnit.getName());
        holder.productPrice.setText("\u20a6 "+listUnit.getPrice());
        holder.productName.setTextColor(Color.parseColor(listUnit.getColor()));
        holder.productPrice.setTextColor(Color.parseColor(listUnit.getColor()));
        //i would have to use another method to load images
        //using Picasso
        Picasso.with(context)
                .load(Config.IMAGE_URL+listUnit.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.productImage);

    holder.detailLayout.setBackgroundColor(Color.parseColor(listUnit.getViewColor()));
    }

    @Override
    public int getItemCount() {
        return listUnitList.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView productImage;
        public LinearLayout detailLayout;
        public TextView productName, productPrice;

        public MyViewHolder(View itemView) {
            super(itemView);

            //define views i want to fill up with something here
            productImage = itemView.findViewById(R.id.productImage);
            detailLayout=itemView.findViewById(R.id.detailLayout);
            productName=itemView.findViewById(R.id.productName);
            productPrice=itemView.findViewById(R.id.productPrice);
        }
    }
}
