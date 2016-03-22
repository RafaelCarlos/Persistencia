package com.example.rafaelcarlos.persistenciarealm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rafaelcarlos.persistenciarealm.AddUpdateDiscipline;
import com.example.rafaelcarlos.persistenciarealm.R;
import com.example.rafaelcarlos.persistenciarealm.model.Discipline;

import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by rafaelcarlos on 08/03/16.
 */
public class DisciplineAdapter extends RealmBaseAdapter<Discipline> {


    public DisciplineAdapter(Context context, RealmResults<Discipline> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_discipline, parent, false);

            holder = new CustomViewHolder();
            convertView.setTag(holder);

            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.btUpdate = (Button) convertView.findViewById(R.id.bt_update);
            holder.btRemove = (Button) convertView.findViewById(R.id.bt_remove);


        } else {
            holder = (CustomViewHolder) convertView.getTag();

        }

        final Discipline d = realmResults.get(position);

        holder.tvName.setText(d.getNome());

        holder.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, AddUpdateDiscipline.class);
                it.putExtra(Discipline.ID, d.getId());
                context.startActivity(it);
            }
        });

        holder.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getInstance(context);

                realm.beginTransaction();
                d.removeFromRealm();
                realm.commitTransaction();
                realm.close();
            }
        });


        return convertView;
    }

    private static class CustomViewHolder {
        TextView tvName;
        Button btUpdate;
        Button btRemove;
    }

}
