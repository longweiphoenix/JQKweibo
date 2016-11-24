package com.example.along;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.along.jqk.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class MyAdapter extends BaseAdapter{
    List<WeiBoHome> list;
    Context context;
    LayoutInflater layoutInflater;
    public MyAdapter(List<WeiBoHome> list, Context context) {
        this.list=list;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.weibo_item,null);
            viewHolder.commentNum= (TextView) convertView.findViewById(R.id.comment_num);
            viewHolder.text= (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        }
        viewHolder= (ViewHolder) convertView.getTag();
        WeiBoHome weiBoHome=list.get(position);
        viewHolder.commentNum.setText(weiBoHome.getCommentNums());
        viewHolder.text.setText(weiBoHome.getContent());
        return convertView;
    }
    private class ViewHolder{
        TextView commentNum,text;
    }
}
