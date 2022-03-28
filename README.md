## sbt project compiled with Scala 3

### Usage

1. Requirements:
    - python 3.5 or higher (best YT scraper I found in this language, Scala runs it as subprocess)
    https://pypi.org/project/youtube-transcript-api/
    - Scala 3 (with dependencies as in build.sbt file)
    - 2 OpenNLP models - English parts of speech and tokens
    https://opennlp.apache.org/models.html

2. Deployment:
    - run setup.sh bash file (creates python virtual environment and downloads scraper)
    - file should contain codes only separated by '\n'

3. Running:
    - as arguments, provide path to file containing codes and integer
    - program reuses articles - if its already in "articles" folder, it just copy it to
    output file
    - start - splits codes in files on '\n' character
    - scraping YT captions
    - finding nouns
    - scraping wikipedia by noun
    - forming final output
    - append data to file, named by by its youtube code
    - results are XML files in "outputs" directory

4. Packages:
    - files in main are objects, grouped by their purpose
    - "ML" stands for machine learning
    - "temps" are classes which are used as temporary data in chains pf functions

5. What else can be done:
    - Handling disambiguation pages in wikipedia (they are not standarized)
    - third argument - output directory
    - timestamps for wiki articles - if they expire, articles is removed and new version is downloaded

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.


