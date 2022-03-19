package temps


case class WikiEntry(val noun: String, val link: String, val hasArticle: Boolean)


case class FinalOutput(val code: String, 
            val rawCaptions: String, 
            val wikiEntries: List[WikiEntry])