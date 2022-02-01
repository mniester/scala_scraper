from sys import stdout, stdin

from youtube_transcript_api import YouTubeTranscriptApi
from youtube_transcript_api.formatters import TextFormatter

for code in stdin:
    result = TextFormatter().format_transcript(transcript = YouTubeTranscriptApi.get_transcript(code))
    stdout.write(result)