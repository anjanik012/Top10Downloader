# Top10Downloader
A basic android application created and maintained for practising android development.

The current app reads an RSS feed from : https://www.apple.com/rss/

The Structure of the app is as follows:

 :: A DownloadData class which extends AsyncTask to download the xml data in a backgroud thread.
 
 :: A ParseApplications class to parse the xml data using XmlPullParserFactory(http://www.xmlpull.org/).
 
 :: A custom adapter for a ListView to present the parsed data on the screen.
 
 :: An additional class FeedEntry which is used to store parsed data and pass it around.
