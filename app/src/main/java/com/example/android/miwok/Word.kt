package com.example.android.miwok

/**
 * [Word] represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 */
class Word {

    /**
     * Default translation for the word
     */
    var defaultTranslation: String
        private set

    /**
     * Miwok translation for the word
     */
    var miwokTranslation: String
        private set

    /**
     * Image resource ID for de word
     */
    var imageResourceId = NO_IMAGE_PROVIDED
        private set

    /**
     * Audio resource ID for the word
     */
    var audioResourceId: Int
        private set

    /**
     * @param defaultTranslation is the word in a language that the user is already familiar with
     * (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     */
    constructor(defaultTranslation: String, miwokTranslation: String, audioResourceId: Int) {
        this.defaultTranslation = defaultTranslation
        this.miwokTranslation = miwokTranslation
        this.audioResourceId = audioResourceId
    }

    /**
     * @param defaultTranslation is the word in a language that the user is already familiar with
     * (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     * @param imageResourceId    is the drawable resource ID for the image associated with the word
     */
    constructor(
        defaultTranslation: String,
        miwokTranslation: String,
        imageResourceId: Int,
        audioResourceId: Int
    ) {
        this.defaultTranslation = defaultTranslation
        this.miwokTranslation = miwokTranslation
        this.imageResourceId = imageResourceId
        this.audioResourceId = audioResourceId
    }

    /**
     * Returns whether or not there is an image for this word
     */
    fun hasImage(): Boolean {
        return imageResourceId != NO_IMAGE_PROVIDED
    }

    companion object {
        /**
         * Constant value that represents no image was provided for this word
         */
        private const val NO_IMAGE_PROVIDED = -1
    }
}