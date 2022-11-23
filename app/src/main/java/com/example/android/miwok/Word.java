package com.example.android.miwok;

/**
 * {@link Word} represents a vocabulary word that the user wants to learn.
 * It contains a default translation and a Miwok translation for that word.
 */
public class Word {

    /**
     * Default translation for the word
     */
    private String defaultTranslation;

    /**
     * Miwok translation for the word
     */
    private String miwokTranslation;

    /**
     * @param defaultTranslation is the word in a language that the user is already familiar with
     *                           (such as English)
     * @param miwokTranslation   is the word in the Miwok language
     */
    public Word(String defaultTranslation, String miwokTranslation) {
        this.defaultTranslation = defaultTranslation;
        this.miwokTranslation = miwokTranslation;
    }

    /**
     * Get the default translation of the word.
     */
    public String getDefaultTranslation() {
        return defaultTranslation;
    }

    /**
     * Get the Miwok translation of the word.
     */
    public String getMiwokTranslation() {
        return miwokTranslation;
    }


}
