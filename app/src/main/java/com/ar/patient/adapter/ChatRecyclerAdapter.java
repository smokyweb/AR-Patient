package com.ar.patient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ar.patient.R;
import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;
import com.ar.patient.helper.Utils;
import com.ar.patient.responsemodel.Chat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;
    Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
    private ArrayList<Chat> mChats = new ArrayList<>();
    private onAnsweerclicklistener clicklistener;
    private int checkclicked = 0;
    public String PatientName;
    public String PatientImg;

    public ChatRecyclerAdapter(Context context, String PatientName, String PatientImg, onAnsweerclicklistener clicklistener) {
        this.PatientName = PatientName;
        this.PatientImg = PatientImg;
        this.context = context;
        this.clicklistener = clicklistener;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mChats.get(position).isIsme()) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        try {
            Chat chat = mChats.get(position);
            myChatViewHolder.txtUserMsg.setText(chat.getMessege());
            myChatViewHolder.txtUserName.setText(Pref.getValue(context, Config.PREF_NAME, ""));
            Utils.loadImage(context, Pref.getValue(context, Config.PREF_AVATAR, ""), R.drawable.avtar, myChatViewHolder.ImgUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        try {
            Chat chat = mChats.get(position);
            otherChatViewHolder.txtotherUserMsg.setText(chat.getMessege());
            otherChatViewHolder.txtotherUserName.setText(PatientName);
            Utils.loadImage(context, PatientImg, R.drawable.avtar, otherChatViewHolder.ImgOtherUseravatar);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).isIsme()) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public void addall(ArrayList<Chat> chats) {
        mChats.addAll(chats);
        notifyDataSetChanged();
    }

    public interface onAnsweerclicklistener {
        void onanswerClicked(String bean);
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName, txtUserMsg;
        private ImageView ImgUser;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtUserMsg = (TextView) itemView.findViewById(R.id.txtUserMsg);
            ImgUser = (ImageView) itemView.findViewById(R.id.ImgUser);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtotherUserName, txtotherUserMsg;
        private ImageView ImgOtherUseravatar;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtotherUserName = (TextView) itemView.findViewById(R.id.txtotherUserName);
            txtotherUserMsg = itemView.findViewById(R.id.txtotherUserMsg);
            ImgOtherUseravatar = itemView.findViewById(R.id.ImgOtherUseravatar);
        }
    }
}
