package com.sun.borobhai.data

data class Language(
    val name: String,
    val definition: String,
    val whyLearn: String,
    val workingField: String,
    val bestBooks: List<String>,
    val bestEditors: List<String>,
    val bestYouTubeChannels: List<String>,
    val onlineCompilers: List<String>,
    val booksDownloadLinks: List<String>,
    val editorsDownloadLinks: List<String>,
    val onlineCompilersLink: List<String>,
    val youtubeChannelsLinks: List<String>
)