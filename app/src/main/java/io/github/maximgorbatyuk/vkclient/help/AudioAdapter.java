package io.github.maximgorbatyuk.vkclient.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.maximgorbatyuk.vkclient.R;

/**
 * Created by Maxim on 24.04.2016.
 */
public class AudioAdapter extends ArrayAdapter<Audio> {

    private Context context;
    private List<Audio> source;

    public AudioAdapter(Context context, List<Audio> list) {
        super(context, R.layout.audio_record, list);
        this.context = context;
        source = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View audioItem = inflater.inflate(R.layout.audio_record, parent, false);

        TextView title      = (TextView) audioItem.findViewById(R.id.record_title);
        TextView artist     = (TextView) audioItem.findViewById(R.id.record_artist);
        TextView duration   = (TextView) audioItem.findViewById(R.id.record_duration);

        Audio record = source.get(position);
        title.setText(record.title);
        artist.setText(record.artist);
        duration.setText( Application.transformDuration(record.duration) );

        return audioItem;
        //return super.getView(position, convertView, parent);
    }
}
