from sys import stdout, stdin

from youtube_transcript_api import YouTubeTranscriptApi
from youtube_transcript_api.formatters import TextFormatter

for code in stdin:
    try:
        result = ' ' + TextFormatter().format_transcript(transcript = YouTubeTranscriptApi.get_transcript(code))
    except:
        result = "Error"
    stdout.write(result)
    stdout.flush()