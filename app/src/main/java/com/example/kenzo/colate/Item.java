package com.example.kenzo.colate;

public class Item {
        // 記事のタイトル
        private CharSequence mTitle;
        // 記事の本文
        private CharSequence mDescription;

        private CharSequence mUrl;

        public CharSequence getDescription() {
            return mDescription;
        }

        public void setDescription(CharSequence description) {
            mDescription = description;
        }

        public CharSequence getTitle() {
            return mTitle;
        }

        public void setTitle(CharSequence title) {
            mTitle = title;
        }

    public CharSequence getUrl() {
        return mUrl;
    }

    public void setUrl(CharSequence mUrl) {
        this.mUrl = mUrl;
    }
}
