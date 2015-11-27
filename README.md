# Popular-Movies

I have removed the API key from this file. Edit the FetchMovieTask.java file.
There is a variable in the file named <b>"apiKey"</b> at the top of the file. Simply
put in your personal key and the application will run.

Alternatively, you can follow these instructions to create your API key in your
gradle file. This was taken from the Udacity community forum.

Open or create C:\Users\USERNAME\.gradle\gradle.properties
Add a line saying TheMovieDBAPIKey=YOUR_API_KEY
In your module's build.gradle file, add the following.

def THE_MOVIE_DB_API_KEY = '"' + TheMovieDBAPIKey + '"' ?: '"Missing The Movie DB API Key"';

and add to the all section in buildTypes

buildTypes {
    all {
        buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', THE_MOVIE_DB_API_KEY
    }
    (...)
}
To access the API key, in your code use BuildConfig.THE_MOVIE_DB_API_KEY
