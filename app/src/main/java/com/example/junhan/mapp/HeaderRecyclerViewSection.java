package com.example.junhan.mapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class HeaderRecyclerViewSection extends StatelessSection{
    private String title;
    private ArrayList<EventsItem> eventList;
    private onItemClickListener mListener;
    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.subsharedprefs";

    HeaderRecyclerViewSection(Context context, String title, ArrayList<EventsItem> eventList) {
        super(R.layout.events_header, R.layout.event_item);
        this.title = title;
        this.eventList = eventList;
        mPreferences = context.getSharedPreferences(sharedPrefFile, context.MODE_PRIVATE);
    }

    @Override
    public int getContentItemsTotal() {
        return eventList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view, mListener);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder)holder;
        hHolder.headerTitle.setText(title);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder iHolder = (ItemViewHolder) holder;
        EventsItem currentItem = eventList.get(position);

        SimpleDateFormat convTime = new SimpleDateFormat("HH:mm"); //convTime.format
        SimpleDateFormat strToDate = new SimpleDateFormat("dd/MM/yyyy HH:mm"); //strToDate.parse

        //check if user has indicated attendance for event, and draw appropriate icon
        if (mPreferences.getString(currentItem.getName(), "") == "1"){
            iHolder.mImageView.setImageResource(R.drawable.ic_check);
        } else {
            iHolder.mImageView.setImageResource(R.drawable.ic_plus_blue);
        }

        //set text for attendance and name of event
        iHolder.mTextViewAttend.setText(Integer.toString(currentItem.getAttendance()));
        iHolder.mTextView1.setText(currentItem.getName());

        //set timestart, timeend and location of event (all in one line yes). If the dateStart part is confusing, see the comment in EventsItem class
        try {
            iHolder.mTextView2.setText(convTime.format(strToDate.parse(currentItem.getDateStart())) + " - " + convTime.format(strToDate.parse(currentItem.getDateEnd()))
                    + "   |   " + currentItem.getLocation());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
        void onCheckClick(View view, int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }


}