 > **Nikita Kozino**   
 >    
 >  __MongoDB + MySQL__   
 >  
 >  For the MySQL: I wrote a statement which creates a table articles and stores the titles and dates, which is executed after the titles and dates are parsed. Afterwards, I wrote the queries searching for the keywords "cancer" and "obesity" for years 2017-2019, and a query for a range of dates between 2018 and 2019.
 >  For the MongoDB, I implemented something very similar, the only difference being that I appended the elements to a document which I then add to an ArrayList of documents, which then is added to a collection. And I wrote a method called mongoSearch that implements similar queries I mentioned above.
