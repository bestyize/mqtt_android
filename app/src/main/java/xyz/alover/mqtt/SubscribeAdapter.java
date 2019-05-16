package xyz.alover.mqtt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.ViewHolder> {

    private List<Subscribe> subscribeList;
    private Context context;
    private MqttManager mqttManager;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_subscribe_list,tv_subscribe_message;
        Button btn_subscribe;
        LinearLayout ll_subscribe_status;


        public ViewHolder(@NonNull View view) {
            super(view);
            tv_subscribe_list=(TextView)view.findViewById(R.id.tv_subscribe_list);
            tv_subscribe_message=(TextView)view.findViewById(R.id.tv_subscribe_message);
            btn_subscribe=(Button)view.findViewById(R.id.btn_subscribe);
            ll_subscribe_status=(LinearLayout)view.findViewById(R.id.ll_subscribe_status);


        }
    }

    public SubscribeAdapter(List<Subscribe> subscribeList,MqttManager mqttManager){
        this.subscribeList=subscribeList;
        this.mqttManager=mqttManager;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.subscribe_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Subscribe subscribe=subscribeList.get(i);
        if(subscribe.isSubscribeStatus()==true){
            viewHolder.ll_subscribe_status.setVisibility(View.VISIBLE);
        }else {
            viewHolder.ll_subscribe_status.setVisibility(View.GONE);
        }
        viewHolder.tv_subscribe_list.setText(subscribe.getTopic());
        viewHolder.tv_subscribe_message.setText(subscribe.getMessage());

        viewHolder.btn_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscribe.isSubscribeStatus()==true){
                    mqttManager.unSubscribe(subscribe.getTopic());
                    viewHolder.btn_subscribe.setText("订阅");
                    viewHolder.ll_subscribe_status.setVisibility(View.GONE);
                    subscribe.setSubscribeStatus(false);
                }else {
                    mqttManager.subscribe(subscribe.getTopic(),false,0);
                    viewHolder.btn_subscribe.setText("已订阅");
                    viewHolder.ll_subscribe_status.setVisibility(View.VISIBLE);
                    subscribe.setSubscribeStatus(true);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return subscribeList.size();
    }


}
