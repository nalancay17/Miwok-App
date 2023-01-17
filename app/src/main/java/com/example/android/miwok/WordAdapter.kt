package com.example.android.miwok

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class WordAdapter(
    context: Context,
    words: List<Word>,
    /**
     * Resource ID for the background color to this list of words
     */
    private val colorResourceId: Int

) : ArrayAdapter<Word>(context, 0, words) {
// 0 porque no usaremos ningun layout como base para los diseños de las Views que producira el adapter


    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param _convertView The recycled view to populate. (the view to be reused)
     * (search for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The view for the position in the AdapterView.
     */
    override fun getView(position: Int, _convertView: View?, parent: ViewGroup): View {
        // Get the [Word] object located at this position in the list
        val word = getItem(position)
        // Initialize by checking if an existing view is being reused, otherwise inflate the view
        val convertView =
            _convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        if(word != null) {
            setTexts(word, convertView)
            setImage(word, convertView)
            setColor(convertView)
        }

        // Return the completed view to render on screen
        return convertView
    }

    private fun setTexts(word: Word, convertView: View) {
        // Lookup view for data population
        val miwokTranslation = convertView.findViewById<TextView>(R.id.miwok_textview)
        val defaultTranslation = convertView.findViewById<TextView>(R.id.default_textview)

        // Populate the data into the template view using the data object
        miwokTranslation.text = word.miwokTranslation
        defaultTranslation.text = word.defaultTranslation
    }

    private fun setImage(word: Word, convertView: View) {
        val itemImage = convertView.findViewById<ImageView>(R.id.item_image)
        if (word.hasImage())
            itemImage.setImageResource(word.imageResourceId)
        else
            itemImage.visibility = View.GONE
    }

    private fun setColor(convertView: View) {
        val itemContainer = convertView.findViewById<View>(R.id.items_text_layout)
        // Find the color that the resource ID maps to
        val color = ContextCompat.getColor(context, colorResourceId)
        itemContainer.setBackgroundColor(color)
    }
}