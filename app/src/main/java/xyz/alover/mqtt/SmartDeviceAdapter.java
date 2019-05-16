package xyz.alover.mqtt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SmartDeviceAdapter extends RecyclerView.Adapter<SmartDeviceAdapter.ViewHolder> {
    private List<SmartDevice> smartDeviceList;
    private Context context;
    private MqttManager mqttManager;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_device_name,tv_device_detail;
        ImageView iv_device_logo;
        Button btn_device_switch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_device_name=(TextView)itemView.findViewById(R.id.tv_device_name);
            tv_device_detail=(TextView)itemView.findViewById(R.id.tv_device_detail);
            iv_device_logo=(ImageView)itemView.findViewById(R.id.iv_device_logo);
            btn_device_switch=(Button)itemView.findViewById(R.id.btn_device_switch);
        }
    }

    public SmartDeviceAdapter(List<SmartDevice> smartDeviceList,MqttManager mqttManager){
        this.smartDeviceList=smartDeviceList;
        this.mqttManager=mqttManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.device_item,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final SmartDevice smartDevice=smartDeviceList.get(i);
        holder.iv_device_logo.setImageResource(smartDevice.getImageId());
        holder.tv_device_detail.setText(smartDevice.getDetail());
        holder.tv_device_name.setText(smartDevice.getName());
        holder.btn_device_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo
                String currentState=holder.btn_device_switch.getText().toString();
                if(currentState.contains("开")){
                    holder.btn_device_switch.setText("关");
                    mqttManager.publish(smartDevice.getSmartDeviceMqttInfo().getTopic(),smartDevice.getSmartDeviceMqttInfo().getMessageOff(),false,0);

                }else if(currentState.contains("关")){
                    holder.btn_device_switch.setText("开");
                    mqttManager.publish(smartDevice.getSmartDeviceMqttInfo().getTopic(),smartDevice.getSmartDeviceMqttInfo().getMessageOn(),false,0);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return smartDeviceList.size();
    }


}
