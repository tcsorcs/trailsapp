package com.tcsorcs.trailsapp.helpers;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.tcsorcs.trailsapp.R;
        import com.tcsorcs.trailsapp.helpers.Achievement;
        import com.tcsorcs.trailsapp.managers.DisplayManager;

        import java.util.ArrayList;

public class AchievementArrayAdapter<Achievement> extends ArrayAdapter<Achievement> {
    private final Context context;
    private final ArrayList<Achievement> values;

    public AchievementArrayAdapter(Context context, ArrayList<Achievement> values) {
        super(context, R.layout.listview_achievement, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.listview_achievement, parent, false);

        com.tcsorcs.trailsapp.helpers.Achievement ach=((com.tcsorcs.trailsapp.helpers.Achievement)values.get(position));


        TextView textView = (TextView) rowView.findViewById(R.id.achievementRowTitle);
        String title=ach.getName();
        textView.setText(title);

        textView = (TextView) rowView.findViewById(R.id.achievementRowDate);
        String date=ach.getDateAchieved();
        textView.setText(date);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        int resId  = DisplayManager.getInstance().main_activity.getResources().getIdentifier(ach.getIcon(), "drawable", DisplayManager.getInstance().main_activity.getPackageName());


        imageView.setImageResource(resId);


        return rowView;
    }
}