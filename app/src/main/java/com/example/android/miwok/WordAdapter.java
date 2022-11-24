package com.example.android.miwok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {

    /**
     * Resource ID for the background color to this list of words
     */
    private int colorResourceId;

    public WordAdapter(Context context, List<Word> words, int colorResourceId) {
        // 0 porque no usaremos ningun layout como base para los diseños de las Views que producira el adapter
        super(context, 0, words);
        this.colorResourceId = colorResourceId;
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate. (the view to be reused)
     *                    (search for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The view for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the {@link Word} object located at this position in the list
        Word word = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        setTexts(word, convertView);
        setImage(word, convertView);
        setColor(convertView);

        // Return the completed view to render on screen
        return convertView;
    }

    private void setTexts(Word word, View convertView) {
        // Lookup view for data population
        TextView miwokTranslation = convertView.findViewById(R.id.miwok_textview);
        TextView defaultTranslation = convertView.findViewById(R.id.default_textview);

        // Populate the data into the template view using the data object
        miwokTranslation.setText(word.getMiwokTranslation());
        defaultTranslation.setText(word.getDefaultTranslation());
    }

    private void setImage(Word word, View convertView) {
        ImageView itemImage = convertView.findViewById(R.id.item_image);
        if (word.hasImage())
            itemImage.setImageResource(word.getImageResourceId());
        else
            itemImage.setVisibility(View.GONE);
    }

    private void setColor(View convertView) {
        View itemContainer = convertView.findViewById(R.id.items_text_layout);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), colorResourceId);
        itemContainer.setBackgroundColor(color);
    }

}
