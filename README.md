# Proiect GlobalWaves  - Etapa 3

<div align="center"><img src="https://tenor.com/view/listening-to-music-spongebob-gif-8009182.gif" width="300px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa3](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa3)

## Implementation
In making the third part of the homework I used my implementations from the second part.
Besides the Singleton pattern that I've implemented on the Admin class, at this part of
the homework I've implemented the Command, Factory and Visitor design patterns. 
I've used Visitor pattern in the implementation of the page system, because the Visitor
pattern separates the logic for displaying pages from the page classes themselves.
This promotes a clear separation of concerns, making the code more modular and easier 
to maintain. If you introduce new page types, you can simply create a new visitor 
implementation for the new types without modifying the existing visitor or page classes.
This makes it easy to extend your system with new features.

In the making of the statistics I've used the Factory design pattern because The client
code (the code that generates statistics) does not need to know the details of how each 
type of statistics object is created. It relies on the abstraction provided by the 
StatisticsFactory interface. This encapsulation allows for changes in the concrete 
classes (like adding new types of statistics) without affecting the client code. The use 
of a factory provides a clear and readable way to create instances of different statistics 
types based on the user's type.

In the making of the subscribe method I've used the Command design pattern becauseIn the
making of the subscribe method, I've used the Command design pattern because it provides 
a structured and flexible way to encapsulate the subscription operation. The Command
pattern proves valuable in managing and organizing complex user interactions, providing a
clear and structured approach to handling subscription-related operations.

For the implementation of the page system I've preferred the Visitor design pattern because
it separates the logic for displaying pages from the page classes themselves. This promotes
a clear separation of concerns, making the code more modular and easier to maintain. The
pattern adheres to the Open/Closed Principle, allowing you to add new operations (visitors) 
without modifying the existing page classes. This is beneficial for extensibility, as you 
can introduce new functionality without altering the existing codebase. The pattern allows 
you to define different visitors for different purposes. For example, you could have a 
PageExportVisitor for exporting pages, a PageValidationVisitor for validating pages, etc. 
This flexibility makes it easy to extend the functionality of your system without modifying 
existing code.


## New functionalities added

* Wrapped (shows a resume for the user's activity):
  * Stats for users
  * Stats for artists
  * Stats for hosts
* Monetization - a way of paying the artists for their work by:
  * Ability to buy merch
  * Listening to their songs via premium account/listening to adds
* Notifications - every time an artist/host adds something their subscribers are notified
* Subscribe - the ability to subscribe to an artist/host
* Get Notifications - the ability to check your notifications
* User recommendations - there are 3 types of recommendations:
  * Random Song: it will generate a song based on the genre of the song that the user is 
  currently listening to
  * Fans Playlist: it will generate a playlist based on the likes of the top 5 fans of the 
  artist that is currently listened to
  * Random Playlist: it is generated from the top 3 genres of the user (based on likes, 
  created playlists, followed playlists)
* Load Recommendations - the ability to listen to the last recommendation that was made 
for the user
* Page Navigation - the ability to navigate through different pages:
  * changePage has now the ability to redirect the user on the artist's/host's 
  (that the user is currently listening to) page
  * nextPage will direct the user on the next page in the user's history
  * previousPage will direct the user on the previous page in user's history

